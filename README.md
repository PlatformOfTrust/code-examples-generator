# Code Examples Generator

[![Build Status](https://travis-ci.org/PlatformOfTrust/code-examples-generator.svg?branch=master)](https://travis-ci.org/PlatformOfTrust/code-examples-generator)
[![CHANGELOG.md](https://img.shields.io/badge/-changelog-blue.svg)](CHANGELOG.md)

The purpose of this project is to create a command line tool that is able to 
parse Platform of Trust API documentation and examples from [RAML 1.0](RAML-spec) 
files and generate example HTTP requests in various languages.

For instructions how to use this tool see [USER GUIDE][guide]. 

See also [Code Examples Validator][validator] and implementation (`.travis.yml`) 
in the [PlatformOfTrust/docs][docs] repository.

## Getting Started

These instructions will get you a copy of the project up and running on your 
local machine for development and testing purposes.

### Prerequisites

This is a [Clojure][clj] project and requires:

1. [Java 8 or above][jdk]
2. [Leiningen 2.0 or above][lein] (OSX users can use `brew install leiningen`).

You can make sure that everything is installed by running `java --version && lein -v`.

```
$ java --version && lein -v
java 11.0.2 2018-10-16 LTS
Java(TM) SE Runtime Environment 18.9 (build 11.0.2+7-LTS)
Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.2+7-LTS, mixed mode)
Leiningen 2.9.1 on Java 11.0.2 Java HotSpot(TM) 64-Bit Server VM
```

### Running the application

`lein run` will run the code example generator (and install dependencies). Without 
passing any extra parameters it will display command line help.

```
 $lein run
  -s, --source PATH                     Required RAML file or a directory that contains RAML files.
  -d, --dest PATH      ./pot-examples   Optional Directory for generated code examples.
  -H, --host HOST      api.oftrust.net  Optional URI host.
  -S, --scheme SCHEME  https            Optional URI scheme (`https` or `http`).
  -h, --help
  -v, --version
```

See [user guide][guide] for more details how to use it.

## Testing

```
lein test                               # Run unit tests
lein test-refesh                        # Run unit tests automatically when files change
lein cloverage                          # Generate code coverage report
```

NB! This project is expected to have > 50% code coverage for unit tests and it 
has been set as a criteria for successful builds in CI. You can change it in 
[project.clj](project.clj).

```
  :cloverage {:fail-threshold 50
              :low-watermark 70
              :high-watermark 90}
```

### e2e tests

You can run generated code examples against [mockbin.org](mockbin.org) to 
validatate whether examples are semantically correct e.g. there are no typos 
and code examples are valid examples for their respective languages. 

```
  sh ./scripts/generate_examples.sh     # Generate code examples
  npm install unirest                   # Install unirest
  sh ./scripts/e2e_tests/nodejs.sh      # Test node.js/unirest examples 
  sh ./scripts/e2e_tests/python.sh      # Test Python examples
  sh ./scripts/e2e_tests/curl.sh        # Test cURL examples
```

NB! When running node.js tests locally a path to installed node_modules needs 
to be provided. This the path where `npm install unirest` was triggered.

`NODE_PATH=/Users/sven/dev/PlatformOfTrust/code-examples-generator/node_modules ./scripts/e2e_tests/nodejs.sh`



<!-- ### Integration tests -->

<!-- This tool will generate HTTP request examples according to provided (HTTP  -->
<!-- request) templates and API documenation in RAML. Unit tests should be  -->
<!-- sufficient to make sure that generate examples have been created correctly but  -->
<!-- they cannot guarantee that requests will actually work in their respetive  -->
<!-- environments due to errors in either documentation or templates. -->

<!-- Integration tests will take the generated HTTP request examples and run them  -->
<!-- against [Mockbin](mockbin) HTTP endpoints to make sure that requests work in  -->
<!-- their respective environments. -->

<!-- TODO! More details about setup and running. -->

<!-- Passing integration tests is a requirement for successful builds in CI! -->

## Deployment

Each commit to master branch will trigger a new build process that will build a 
binary (jar file `raml2http.jar`) that will be uploaded to the assets of 
[latest release][releases-latest]. 

Each tag will create a [release][releases] with tag name and upload jar file 
with version suffix e.g. `raml2http-v1.0.1.jar`.

## Development flow

1. Create a feature branch. Implement changes. Test.
2. Update e2e test if adding new templates (see `.travis.yml`).
3. Bump the version in project.clj
4. Update [Change Log](./CHANGELOG.md).
5. Merge feature/bugifix branch to master
6. Create a [new release](new-release). This will trigger a new build in CI 
which will upload a new jar file e.g. `raml2http-v1.0.1.jar` to release 
assests in a few minutes. 

## Contributing

You might want to...

- Follow [The Clojure Style Guide][bbatsov] for consistency.
- Commit and create PRs using [Conventional Commits standard](cnvc).
- Use [SemVer](semver).
- Update [Change Log](./CHANGELOG.md).

```
lein eastwood                           # Linter
lein kibit                              # Static code analyzer
lein bikeshed                           # Gives tips for writing better code
lein ancient                            # Check for outdated dependencies
lein annotations                        # Display all comment annotations (TODO, FIXME etc.)
```

## Acknowledgements/future improvements

This project does not support parsing all the options according to 
[RAML 1.0 spec](RAML-spec). There are too many nuances and options that did 
not fit the scope of this project. 

Some examples:
- This tool currently supports only single examples for responses and Types.
- ...

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.

--------------------------------------------------------------------------------
Copyright © 2019 Platform Of Trust

[RAML-spec]: https://github.com/raml-org/raml-spec/blob/master/versions/raml-10/raml-10.md
[clj]: https://clojure.org/
[jdk]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
[lein]: https://leiningen.org/
[mockbin]: http://mockbin.org/
[bbatsov]: https://github.com/bbatsov/clojure-style-guide
[semver]: http://semver.org/
[cnvc]: https://www.conventionalcommits.org/
[guide]: ./doc/README.md
[releases-latest]: https://github.com/PlatformOfTrust/code-examples-generator/releases/tag/latest
[releases]: https://github.com/PlatformOfTrust/code-examples-generator/releases
[validator]: https://github.com/PlatformOfTrust/code-examples-validator
[docs]: https://github.com/PlatformOfTrust/docs
[new-release]: https://github.com/PlatformOfTrust/code-examples-generator/releases/new
