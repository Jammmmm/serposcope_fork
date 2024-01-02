/*
 * Serposcope - SEO rank checker https://serposcope.serphacker.com/
 *
 * Copyright (c) 2016 SERP Hacker
 * @author Pierre Nogues <support@serphacker.com>
 * @license https://opensource.org/licenses/MIT MIT License
 */
package com.serphacker.serposcope.scraper.google.scraper;

import com.serphacker.serposcope.scraper.captcha.Captcha;
import com.serphacker.serposcope.scraper.captcha.CaptchaImage;
import com.serphacker.serposcope.scraper.captcha.CaptchaRecaptcha;
import com.serphacker.serposcope.scraper.captcha.solver.CaptchaSolver;
import com.serphacker.serposcope.scraper.google.GoogleCountryCode;
import com.serphacker.serposcope.scraper.google.GoogleScrapResult;
import com.serphacker.serposcope.scraper.google.GoogleScrapResult.Status;
import com.serphacker.serposcope.scraper.google.GoogleScrapSearch;
import com.serphacker.serposcope.scraper.http.ScrapClient;
import com.serphacker.serposcope.scraper.http.proxy.DirectNoProxy;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * not thread safe
 * @author admin
 */
public class GoogleScraper {

    public final static int DEFAULT_MAX_RETRY = 3;

    final static BasicClientCookie CONSENT_COOKIE = new BasicClientCookie("CONSENT", "YES+");
    static {
        CONSENT_COOKIE.setDomain("google.com");
        CONSENT_COOKIE.setPath("/");
        CONSENT_COOKIE.setAttribute(ClientCookie.PATH_ATTR, "/");
        CONSENT_COOKIE.setAttribute(ClientCookie.DOMAIN_ATTR, ".google.com");
    }

    public final static String DEFAULT_DESKTOP_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/121.0";
    public final static String DEFAULT_SMARTPHONE_UA = "Mozilla/5.0 (Android 14; Mobile; rv:109.0) Gecko/121.0 Firefox/121.0";

    private static final Logger LOG = LoggerFactory.getLogger(GoogleScraper.class);

    int maxRetry = DEFAULT_MAX_RETRY;
    protected ScrapClient http;
    protected CaptchaSolver solver;
    Random random = new Random();

    Document lastSerpHtml = null;
    int captchas=0;

    public GoogleScraper(ScrapClient client, CaptchaSolver solver) {
//        this.search = search;
        this.http = client;
        this.solver = solver;
    }

    public GoogleScrapResult scrap(GoogleScrapSearch search) throws InterruptedException {
        lastSerpHtml = null;
        captchas = 0;
        List<String> urls = new ArrayList<>();
        prepareHttpClient(search);
        long resultsNumber = 0;

        String referrer = "https://" + buildHost(search) + "/";
        for (int page = 0; page < search.getPages(); page++) {

            if(Thread.interrupted()){
                throw new InterruptedException();
            }

            String url = buildRequestUrl(search, page);

            Status status = null;
            for (int retry = 0; retry < maxRetry; retry++) {
                LOG.debug("GET {} via {} with user-agent {} - try {}", url, http.getProxy() == null ? new DirectNoProxy() : http.getProxy(), http.getUseragent (), retry+1);

                status = downloadSerp(url, referrer, search, retry);
                search.doRandomPagePause (true);
                if(status == Status.OK){
                    status = parseSerp(search, urls);
                    if(status == Status.OK){
                        break;
                    }
                }

                if(!isRetryableStatus(status)){
                    break;
                }
            }

            if(status != Status.OK){
                return new GoogleScrapResult(status, urls, captchas);
            }

            if(page  == 0){
                resultsNumber = parseResultsNumberOnFirstPage();
            }

            if(!hasNextPage()){
                break;
            }
        }
        return new GoogleScrapResult(Status.OK, urls, captchas, resultsNumber);
    }

    protected void prepareHttpClient(GoogleScrapSearch search){
		String ua = "";
		String user_ua = "";

		http.setUseragentType ("");

        switch(search.getDevice()) {
            case DESKTOP:
            	// If a user has entered a custom desktop user agent, use it instead
				user_ua = search.getUADesktop ().trim ();
				ua = (search.getUADesktopDefault ().trim ().equals ("value")) ? user_ua : DEFAULT_DESKTOP_UA;
                http.setUseragent(ua);
                http.setUseragentType ("desktop");
                break;
            case SMARTPHONE:
            	// If a user has entered a custom mobile user agent, use it instead
				user_ua = search.getUAMobile ().trim ();
				ua = (search.getUAMobileDefault ().trim ().equals ("value")) ? user_ua : DEFAULT_SMARTPHONE_UA;
                http.setUseragent(ua);
                http.setUseragentType ("mobile");
                break;
        }

        String hostname = "www.google.com";
        http.removeRoutes();
        if(search.getDatacenter() != null && !search.getDatacenter().isEmpty()){
            http.setRoute(new HttpHost(hostname, -1, "https"), new HttpHost(search.getDatacenter(), -1, "https"));
        }
    }

    protected boolean isRetryableStatus(Status status){
        switch(status){
            case ERROR_CAPTCHA_INCORRECT:
            case ERROR_NETWORK:
                return true;
            default:
                return false;
        }
    }

    protected Status downloadSerp(String url, String referrer, GoogleScrapSearch search, int retry){
        if(referrer == null){
            referrer = "https://www.google.com";
        }

        int status = http.get(url, referrer);
        LOG.info("GOT status=[{}] exception=[{}]", status, http.getException() == null ? "none" :
            (http.getException().getClass().getSimpleName() + " : " + http.getException().getMessage()));
        switch(status){
            case 200:
                return Status.OK;

            case 403:
                try{Thread.sleep((retry+1)*1000);}catch(Exception ex){}
                break;

            case 302:
                ++captchas;
                return handleCaptchaRedirect(url, referrer, http.getResponseHeader("location"));
        }

        return Status.ERROR_NETWORK;
    }

    protected Status parseSerp(GoogleScrapSearch search, List<String> urls){
        String html = http.getContentAsString();
        if(html == null || html.isEmpty()){
            return Status.ERROR_NETWORK;
        }

        lastSerpHtml = Jsoup.parse(html);
        if(lastSerpHtml == null){
            return Status.ERROR_NETWORK;
        }

		Element elDiv = lastSerpHtml.getElementsByTag("body").first();
        if (elDiv != null)
        {
			return parseSerpLayout (search, elDiv, urls);
        }

        return Status.ERROR_PARSING;
    }

    protected Status parseSerpLayoutResLegacy(Element resElement, List<String> urls) {

        Elements h3Elts = resElement.getElementsByTag("h3");
        for (Element h3Elt : h3Elts) {

            if(isSiteLinkElement(h3Elt)){
                continue;
            }

            String link = extractLink(h3Elt.parent());
            if(link == null) {
                link = extractLink(h3Elt.getElementsByTag("a").first());
            }
            if(link != null){
                urls.add(link);
            }
        }

        return Status.OK;
    }

    protected Status parseSerpLayout (GoogleScrapSearch search, Element divElement, List<String> urls) {

		String debugPath = search.getDebugPath ();
		String timestamp = DateTimeFormatter.ofPattern ("yyyy-MM-dd HH-mm-ss").format (LocalDateTime.now ());
		String randomNumber = String.valueOf (Math.round ((Math.random () * 1000 + 1)));
		String filesystemKeyword = search.getKeyword ().replaceAll ("[^A-Za-z0-9]", "");
		String useragentType = http.getUseragentType ().toLowerCase ().trim ();
		String debugBaseFilename = timestamp + "_" + randomNumber + "_" + filesystemKeyword + "_" + useragentType;

		// Dump the elements to a file, if required
		if (!debugPath.equals (""))
		{
			debugDump (debugPath, true, debugBaseFilename + "_before.html", divElement.toString ());
		}

		// Remove the useless elements
		removeUselessElements (search, divElement, useragentType, debugPath, debugBaseFilename + "_details.txt");

		// Dump the elements to a file after removing useless elements, if required
		if (!debugPath.equals (""))
		{
			debugDump (debugPath, true, debugBaseFilename + "_after.html", divElement.toString ());
		}


		// Retrieve the options for mobile or desktop
		String selectors = "", user_selectors = "", selectors_tmp = "";
		String parent = "", user_parent = "", parent_tmp = "";
		String children = "", user_children = "", children_tmp = "";
		if (useragentType.equals ("mobile"))
		{
			// Set the mobile selectors
			selectors = "#main > div > div:nth-of-type(1) a:nth-of-type(1)";
			parent = ".egMi0,.kCrYT";
			children = "cite,h3";

			// If a user has entered custom remove search selectors, use them instead
			user_selectors = search.getLinksParseMobile ().trim ();
			selectors_tmp = search.getLinksParseMobileDefault ().trim ();

			// If a user has entered custom parent CSS selectors, use them instead
			user_parent = search.getLinkHasParentMobile ().trim ();
			parent_tmp = search.getLinkHasParentMobileDefault ().trim ();

			// If a user has entered custom children CSS selectors, use them instead
			user_children = search.getLinkHasChildrenMobile ().trim ();
			children_tmp = search.getLinkHasChildrenMobileDefault ().trim ();
		}
		else if (useragentType.equals ("desktop"))
		{
			// Set the desktop selectors
			selectors = "a[jsname*=\"UWckNb\"]";
			parent = "span[jscontroller=\"msmzHf\"]";
			children = "cite,h3";

			// If a user has entered custom remove search selectors, use them instead
			user_selectors = search.getLinksParseDesktop ().trim ();
			selectors_tmp = search.getLinksParseDesktopDefault ().trim ();

			// If a user has entered custom parent CSS selectors, use them instead
			user_parent = search.getLinkHasParentDesktop ().trim ();
			parent_tmp = search.getLinkHasParentDesktopDefault ().trim ();

			// If a user has entered custom children CSS selectors, use them instead
			user_children = search.getLinkHasChildrenDesktop ().trim ();
			children_tmp = search.getLinkHasChildrenDesktopDefault ().trim ();
		}

		// Sanitize all options
		if (!user_selectors.equals ("") && selectors_tmp.equals ("value")) { selectors = user_selectors.replaceAll ("\"", "\\\""); }
		if (!user_parent.equals ("") && parent_tmp.equals ("value")) { parent = user_parent.replaceAll ("\"", "\\\""); }
		if (!user_children.equals ("") && children_tmp.equals ("value")) { children = user_children.replaceAll ("\"", "\\\""); }

		// Dump the options to a file, if required
		if (!debugPath.equals (""))
		{
			debugDump (debugPath, true, debugBaseFilename + "_details.txt", "selectors: " + selectors + "\nparent: " + parent + "\nchildren: " + children + "\n");
		}

		// Check if any selectors have been provided
		if (!selectors.equals (""))
		{
			// Retrieve the links for the given user agent type
			Elements links = divElement.select(selectors);
			if(links.isEmpty()) {
				return parseSerpLayoutResLegacy(divElement, urls);
			}

			for (Element link : links)
			{
				if(!link.children().isEmpty() && "img".equals(link.child(0).tagName())) {
					continue;
				}

				
				// Check if the link has the required parents available
				if (!parent.equals ("") && link.closest (parent) == null)
				{
					continue;
				}

				// Check if the link has the required children available
				if (!children.equals ("") && link.selectFirst (children) == null)
				{
					continue;
				}

				String url = extractLink(link);
				if(url == null) {
					continue;
				}

				String text = extractText(link);

				// Dump the link to a file, if required
				if (!debugPath.equals (""))
				{
					debugDump (debugPath, true, debugBaseFilename + "_details.txt", "URL: " + url + " - Text: " + text.replaceAll ("[\r\n]", "") + "\n");
				}

				urls.add(url);
			}
		}

        return Status.OK;
    }

    protected Element removeUselessElements (GoogleScrapSearch search, Element divElement, String useragentType, String debugPath, String debugFilename)
    {
		String useless = "", user_useless = "", useless_tmp = "";
		if (useragentType.equals ("mobile"))
		{
			// Set the mobile selectors
			useless = ".qxDOhb,.duf-h,.xpc,.idg8be,.Q71vJc,.BNeawe,.xpx,.X7NTVe," +
			"a[href*=\"maps.google.com\"],.KP7LCb,.Pg70bf,.wEsjbd,.bz1lBb,.cOl4Id,.nBDE1b,.G5eFlf," + 
			"footer,.Gx5Zad>.kCrYT:nth-of-type(2),a[href^=\"/search\\?\"]";

			// If a user has entered custom remove CSS selectors, use them instead
			user_useless = search.getElementsRemoveMobile ().trim ();
			useless_tmp = search.getElementsRemoveMobileDefault ().trim ();
		}
		else if (useragentType.equals ("desktop"))
		{
			// Set the desktop selectors
			useless = ".cUnQKe,#rhs,.DhGrzc,.BDXcec,.mheepd,#appbar,#botstuff," + 
			".iHxmLe,.oIk2Cb,.uVMCKf,.yG4QQe,.TBC9ub,.JNkvid,.uEierd,#taw,.xvfwl,.HiHjCd,.SP6Rje,.YkS8D,.lhLbod," + 
			".gEBHYd,.XqFnDf,.PAq55d,a[href*=\"maps.google.com\"],#easter-egg,#searchform,.Q3DXx,.gke0pe," + 
			".yIbDgf,#sfcnt,.rfiSsc,#hdtbMenus,.fG8Fp,.uo4vr,.lDFEO,.XmmGVd,.iUh30,.LnCrMe,#sfooter,#bfoot," + 
			"#lfootercc,.WZH4jc,.w7LJsc,a[href^=\"/search\\?\"]";

			// If a user has entered custom remove CSS selectors, use them instead
			user_useless = search.getElementsRemoveDesktop ().trim ();
			useless_tmp = search.getElementsRemoveDesktopDefault ().trim ();
		}

		// Sanitize all options
		if (!user_useless.equals ("") && useless_tmp.equals ("value")) { useless = user_useless.replaceAll ("\"", "\\\""); }

		// Remove all useless elements, if at least one selector has been provided
		if (!useless.equals (""))
		{
			// Dump the options to a file, if required
			if (!debugPath.equals (""))
			{
				debugDump (debugPath, true, debugFilename, "remove elements: " + useless + "\n");
			}

			for (Element el : divElement.select (useless))
			{
				el.remove ();
			}
		}

        return divElement;
    }

    protected long parseResultsNumberOnFirstPage(){
        if(lastSerpHtml == null){
            return 0;
        }

        Element resultstStatsDiv = lastSerpHtml.getElementById("resultStats");
        if(resultstStatsDiv == null){
            return 0;
        }

        return extractResultsNumber(resultstStatsDiv.html());
    }


    protected long extractResultsNumber(String html){
        if(html == null || html.isEmpty()){
            return 0;
        }
        html = html.replaceAll("\\(.+\\)", "");
        html = html.replaceAll("[^0-9]+", "");
        if(!html.isEmpty()){
            return Long.parseLong(html);
        }
        return 0;
    }

    protected boolean isSiteLinkElement(Element element){
        if(element == null){
            return false;
        }

        Elements parents = element.parents();
        if(parents == null || parents.isEmpty()){
            return false;
        }

        for (Element parent : parents) {
            if(parent.hasClass("mslg") || parent.hasClass("nrg") || parent.hasClass("nrgw")){
                return true;
            }
        }

        return false;
    }

    protected String extractLink(Element element){
        if(element == null){
            return null;
        }

        String attr = element.attr("href");
        if(attr == null){
            return null;
        }

        if ((attr.startsWith("http://www.google") || attr.startsWith("https://www.google"))){
            if(attr.contains("/aclk?")){
                return null;
            }
        }

        if(attr.startsWith("http://") || attr.startsWith("https://")){
            return attr;
        }

        if(attr.startsWith("/url?")){
            try {
                List<NameValuePair> parse = URLEncodedUtils.parse(attr.substring(5), StandardCharsets.UTF_8);
                Map<String,String> map = parse.stream().collect(Collectors.toMap(NameValuePair::getName,NameValuePair::getValue));
                return map.get("q");
            }catch(Exception ex){
                return null;
            }
        }

        return null;
    }

    protected String extractText(Element element){
        if(element == null){
            return null;
        }

        return element.text ();
    }

    protected boolean hasNextPage(){
        if(lastSerpHtml == null){
            return false;
        }

        if(lastSerpHtml.getElementById("pnnext") != null){
            return true;
        }

        Elements navends = lastSerpHtml.getElementsByClass("navend");
        if(navends.size() > 1 && navends.last().children().size() > 0 && "a".equals(navends.last().child(0).tagName())){
            return true;
        }

        final Elements footerLinks = lastSerpHtml.select("footer a");
        return footerLinks.stream().filter(e -> e.text().endsWith(">")).findAny().isPresent();

    }

    protected String buildRequestUrl(GoogleScrapSearch search, int page){
        String url = "https://";
        try {
            url += buildHost(search) + "/search?q=" + URLEncoder.encode(search.getKeyword(), "utf-8");
        } catch(UnsupportedEncodingException ex){
            url += buildHost(search) + "/search?q=" + search.getKeyword();
        }

        if(search.getCountry() != null && !GoogleCountryCode.__.equals(search.getCountry())){
            url += "&gl=" + search.getCountry().name().toLowerCase();
        }

        String uule = buildUule(search.getLocal());
        if(uule != null){
            url += "&uule=" + uule;
        }

        if(search.getCustomParameters() != null && !search.getCustomParameters().isEmpty()){
            if(search.getCustomParameters().contains("gl=")){
                LOG.warn("custom parameter contains gl= parameter, use country code instead");
            }

            if(!search.getCustomParameters().startsWith("&")){
                url += "&";
            }
            url += search.getCustomParameters();
        }

        if(search.getResultPerPage() != 10){
            url+="&num=" + search.getResultPerPage();
        }

        if(page > 0){
            url+="&start=" + (page*search.getResultPerPage());
        }
        return url;
    }

    protected String buildHost(GoogleScrapSearch search){
        return "www.google.com";
    }

    private final static String UULE_LENGTH = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
    protected String buildUule(String location){
        if(location == null || location.isEmpty()){
            return null;
        }

        byte[] locationArray = location.getBytes();
        if(locationArray.length+1 > UULE_LENGTH.length()){
            LOG.warn("unencodable uule location, length is too long {}", location);
            return null;
        }

        return "w+CAIQICI" +
            UULE_LENGTH.charAt(locationArray.length) +
            Base64.getEncoder().encodeToString(locationArray);
    }

    final static Pattern PATTERN_CAPTCHA_ID = Pattern.compile("/sorry/image\\?id=([0-9]+)&?");
    protected Status handleCaptchaRedirect(String url, String referrer, String redirect){

        http.clearCookies();
        http.addCookie(CONSENT_COOKIE);

        int status = http.get(url, referrer);
        LOG.info("GOT[refetch] status=[{}] exception=[{}]", status, http.getException() == null ? "none" :
            (http.getException().getClass().getSimpleName() + " : " + http.getException().getMessage()));
        if(status == 200){
            return Status.OK;
        }

        if(status == 302){
            redirect = http.getResponseHeader("location");
        }

        if(redirect == null || !redirect.contains("?continue=")){
            return Status.ERROR_NETWORK;
        }

        LOG.debug("captcha form detected via {}", http.getProxy() == null ? new DirectNoProxy() : http.getProxy());
        status = http.get(redirect);
        if(status == 403){
            return Status.ERROR_IP_BANNED;
        }

        if(solver == null){
            return Status.ERROR_CAPTCHA_NO_SOLVER;
        }

        String content = http.getContentAsString();
        if(content == null){
            return Status.ERROR_NETWORK;
        }

        Document doc = Jsoup.parse(content, redirect);

        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            String src = element.attr("abs:src");
            if(src != null && src.contains("/sorry/image")){
                LOG.debug("legacy captcha form detected, trying with captcha image");
                return legacyCaptcha(doc, redirect);
            }
        }

        LOG.debug("trying with captcha recaptcha");
        return recaptchaForm(doc, redirect);
    }

    protected Status recaptchaForm(Document doc, String captchaRedirect){

        Element recaptchaDiv = doc.getElementById("recaptcha");
        if(recaptchaDiv == null){
            LOG.debug("recaptcha div not found");
            return Status.ERROR_NETWORK;
        }

        String dataSitekey = recaptchaDiv.attr("data-sitekey");
        if(dataSitekey.isEmpty()){
            LOG.debug("recaptcha data-sitekey not detected");
            return Status.ERROR_NETWORK;
        }

        String dataS = recaptchaDiv.attr("data-s");
        if(dataS.isEmpty()) {
            LOG.debug("recaptcha data-s not detected");
            return Status.ERROR_NETWORK;
        }

        Element form = recaptchaDiv.parent();
        if(form == null){
            LOG.debug("can't find captcha form (recaptcha)");
            return Status.ERROR_NETWORK;
        }

        Map<String,Object> map = new HashMap<>();
        Elements inputs = form.getElementsByTag("input");
        String formAction = form.attr("abs:action");
        for (Element input : inputs) {

            if("noscript".equals(input.parent().tagName())){
                continue;
            }

            String name = input.attr("name") == null ? "" : input.attr("name");
            String value = input.attr("value") == null ? "" : input.attr("value");

            if(name.isEmpty()){
                continue;
            }

            map.put(name, value);
        }

        if(map.isEmpty()){
            LOG.debug("inputs empty (recaptcha)");
            return Status.ERROR_NETWORK;
        }

        if(formAction == null){
            LOG.debug("form action is null (recaptcha)");
            return Status.ERROR_NETWORK;
        }

        CaptchaRecaptcha captcha = new CaptchaRecaptcha(dataSitekey, dataS, captchaRedirect);
        boolean solved = solver.solve(captcha);
        if(!solved || !Captcha.Status.SOLVED.equals(captcha.getStatus())){
            LOG.error("solver can't resolve captcha error = {}", captcha.getError());
            if(Captcha.Error.SERVICE_OVERLOADED.equals(captcha.getError())){
                LOG.warn("server is overloaded, increase maximum BID on {}", captcha.getLastSolver().getFriendlyName());
            }
            return Status.ERROR_CAPTCHA_INCORRECT;
        }
        LOG.debug("got captcha response {} in {} seconds from {}", captcha.getResponse(), captcha.getSolveDuration()/1000l,
            (captcha.getLastSolver() == null ? "?" : captcha.getLastSolver().getFriendlyName())
        );
        map.put("g-recaptcha-response", captcha.getResponse());

        int postCaptchaStatus = http.post(formAction, map, ScrapClient.PostType.URL_ENCODED, "utf-8", captchaRedirect);
        if(postCaptchaStatus == 302){
            String redirectOnSuccess = http.getResponseHeader("location");
            if(redirectOnSuccess.startsWith("http://")){
                redirectOnSuccess = "https://" + redirectOnSuccess.substring(7);
            }

            int redirect1status = http.get(redirectOnSuccess, captchaRedirect);
            if(redirect1status == 200){
                return Status.OK;
            }

            if(redirect1status == 302){
                if(http.get(http.getResponseHeader("location"), captchaRedirect) == 200){
                    return Status.OK;
                }
            }
        }

        if(postCaptchaStatus == 503){
            LOG.debug("Failed to resolve captcha (incorrect response = {}) (recaptcha)", captcha.getResponse());
//            solver.reportIncorrect(captcha);
        }

        return Status.ERROR_CAPTCHA_INCORRECT;
    }

    protected void debugDump(String path, boolean appendTimestamp, String name, String data){
        if(name == null || data == null){
            return;
        }

		// Set a default path if a valid one has not been given
        if (path == null || path.equals ("") || !Files.isDirectory (Paths.get (path)))
        {
			path = System.getProperty ("java.io.tmpdir");
		}

		// Remove any trailing slashes from the path
		if (path.endsWith (File.separator))
		{
			path = path.substring (0, path.length () - File.separator.length ());
		}

		// Create a timestamped directory, if required
		if (appendTimestamp == true)
		{
			String timestamp = DateTimeFormatter.ofPattern ("yyyy-MM-dd").format (LocalDateTime.now ());
			try
			{
				Files.createDirectories (Paths.get (path + File.separator + timestamp));
				path = path + File.separator + timestamp;	// Only set the new path if it could be successfully created
			}
			catch (Exception ex) { }
		}

        File dumpFile = new File(path + File.separator + name);
        try {
            Files.write(dumpFile.toPath(), data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }catch(Exception ex){
        }
        LOG.debug("debug dump created in {}", dumpFile.getAbsolutePath());
    }

    protected Status legacyCaptcha(Document captchaDocument, String captchaRedirect){

        String imageSrc = null;
        Elements elements = captchaDocument.getElementsByTag("img");
        for (Element element : elements) {
            String src = element.attr("abs:src");
            if(src != null && src.contains("/sorry/image")){
                imageSrc = src;
            }
        }

        if(imageSrc == null){
            LOG.debug("can't find captcha img tag");
            return Status.ERROR_NETWORK;
        }

        Element form = captchaDocument.getElementsByTag("form").first();
        if(form == null){
            LOG.debug("can't find captcha form");
            return Status.ERROR_NETWORK;
        }

        String continueValue = null;
        String formIdValue = null;
        String formUrl = form.attr("abs:action");
        String formQValue = null;

        Element elementCaptchaId = form.getElementsByAttributeValue("name", "id").first();
        if(elementCaptchaId != null){
            formIdValue = elementCaptchaId.attr("value");
        }
        Element elementContinue = form.getElementsByAttributeValue("name", "continue").first();
        if(elementContinue != null){
            continueValue = elementContinue.attr("value");
        }
        Element elementQ = form.getElementsByAttributeValue("name", "q").first();
        if(elementQ != null){
            formQValue = elementQ.attr("value");
        }


        if(formUrl == null || (formIdValue == null && formQValue == null) || continueValue == null){
            LOG.debug("invalid captcha form");
            return Status.ERROR_NETWORK;
        }

        int imgStatus = http.get(imageSrc, captchaRedirect);
        if(imgStatus != 200 || http.getContent() == null){
            LOG.debug("can't download captcha image {} (status code = {})", imageSrc, imgStatus);
            return Status.ERROR_NETWORK;
        }

        CaptchaImage captcha = new CaptchaImage(new byte[][]{http.getContent()});
        boolean solved = solver.solve(captcha);
        if(!solved || !Captcha.Status.SOLVED.equals(captcha.getStatus())){
            LOG.error("solver can't resolve captcha (overload ?) error = {}", captcha.getError());
            return Status.ERROR_CAPTCHA_INCORRECT;
        }
        LOG.debug("got captcha response {} in {} seconds from {}", captcha.getResponse(), captcha.getSolveDuration()/1000l,
            (captcha.getLastSolver() == null ? "?" : captcha.getLastSolver().getFriendlyName())
        );

        try {
            formUrl += "?continue=" + URLEncoder.encode(continueValue, "utf-8");
        }catch(Exception ex){}
        formUrl += "&captcha=" + captcha.getResponse();

        if(formIdValue != null){
            formUrl += "&id=" + formIdValue;
        }
        if(formQValue != null){
            formUrl += "&q=" + formQValue;
        }

        int postCaptchaStatus = http.get(formUrl, captchaRedirect);

        if(postCaptchaStatus == 302){
            String redirectOnSuccess = http.getResponseHeader("location");
            if(redirectOnSuccess.startsWith("http://")){
                redirectOnSuccess = "https://" + redirectOnSuccess.substring(7);
            }

            int redirect1status = http.get(redirectOnSuccess, captchaRedirect);
            if(redirect1status == 200){
                return Status.OK;
            }

            if(redirect1status == 302){
                if(http.get(http.getResponseHeader("location"), captchaRedirect) == 200){
                    return Status.OK;
                }
            }
        }

        if(postCaptchaStatus == 503){
            LOG.debug("reporting incorrect captcha (incorrect response = {})", captcha.getResponse());
            solver.reportIncorrect(captcha);
        }

        return Status.ERROR_CAPTCHA_INCORRECT;
    }

    public ScrapClient getHttp() {
        return http;
    }

    public void setHttp(ScrapClient http) {
        this.http = http;
    }

    public CaptchaSolver getSolver() {
        return solver;
    }

    public void setSolver(CaptchaSolver solver) {
        this.solver = solver;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    public Document getLastSerpHtml() {
        return lastSerpHtml;
    }
}
