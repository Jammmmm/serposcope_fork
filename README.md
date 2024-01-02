# Serposcope Fork

Serposcope is an open source search engine rank checker for SEO. The original homepage, and home to the latest non-forked version, is at : https://serposcope.serphacker.com/

![serposcope rank checker](/serposcope-rank-checker.png)

This fork intends to fix the 2.x series to continue working with Google's changes. It will however eventually be discontinued if/when the latest original version gets the following features:

* Table view
* Ability to import 2.x data
* Becomes open source

## IMPORTANT NOTE

This build is being made public but problems/issues will not be looked at or fixed.

This build is being maintained for myself, and myself only. If it works for me, I'm happy. I will update it as necessary so that it works for me, and will continue to provide those builds to the public.

If it works for you, great! If it does not, sorry.

## Advantage over original

One advantage to this fork is the ability for anyone to continue having a working Serposcope installation even when Google changes their search results, provided you have some knowledge of CSS.

New options for defining custom user agents, as well as HTML and debugging options, are now available for tweaking without having to rebuild Serposcope.

## Building and upgrading

Read [build instructions](/BUILD-INSTRUCTIONS.md)

## Testing

Tests are no longer maintained. I simply do not have the time. Instead I use the following queries for my testing and if they visually look ok, then I consider everything successful:

* **Memoirs of an Invisible Man** - to test removal of a movie, it's plot, cast, etc.
* **Amiga** - to test removal of a computer, it's details, images, etc.
* **6925 Hollywood Blvd Hollywood, CA 90028** - to test removal of a location and Google Maps.
* **Current world events** - to test removal of any news summaries.
* **Movies in cinema** - to test removal of lists.
* **Scrambled eggs recipe** - to test removal of links to recipes.

The following tests would need to be updated based on what is happening at the time:

* **Japan earthquake** - to test removal of news summaries, but potentially containing more information due to being more localized.
* **Wonka showtimes** - to test removal of showtimes, reviews, cast, etc.
* **Liverpool vs Burnley** - to test removal of scores and game summaries.

## License

The MIT License (MIT), see [license](/LICENSE.txt).
