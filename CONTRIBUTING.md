## How to contribute
We'd love to accept your patches and contributions to this project. There are just a few small guidelines you need to follow.

## Preparing a pull request for review
Ensure your change is properly formatted by running:

```gradle
./gradlew spotlessApply
```

Then dump binary API of this library that is public in sense of Kotlin visibilities and ensures that the public binary API wasn't changed in a way that make this change binary incompatible. 

```gradle
./gradlew apiDump
```

Please correct any failures before requesting a review.

## Code reviews
All submissions, including submissions by project members, require review. We use GitHub pull requests for this purpose. Consult [GitHub Help](https://docs.github.com/en/github/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/about-pull-requests) for more information on using pull requests.
