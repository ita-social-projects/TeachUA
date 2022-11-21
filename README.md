![TeachUA](images/logo.png)

# TeachUA BackEnd

[![Github Issues](https://img.shields.io/github/issues/ita-social-projects/TeachUA?style=flat-square)](https://github.com/ita-social-projects/TeachUA/issues)
[![Pending Pull-Requests](https://img.shields.io/github/issues-pr/ita-social-projects/TeachUA?style=flat-square)](https://github.com/ita-social-projects/TeachUA/pulls)
[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

---

## Table of Contents (Optional)

> If your `README` has a lot of info, section headers might be nice.

- [Installation](#installation)
    - [Required to install](#Required-to-install)
    - [Environment](#Environment)
    - [Clone](#Clone)
    - [Setup](#Setup)
    - [How to run local](#How-to-run-local)
- [Usage](#Usage)
    - [How to work with swagger UI](#How-to-work-with-swagger-UI)
    - [How to run tests](#How-to-run-tests)
    - [How to Checkstyle](#How-to-Checkstyle)
- [Documentation](#Documentation))
- [Contributing](#contributing)
    - [git flow](#git-flow)
- [License](#license)

---

## Installation

- All the `code` required to get started
- Images of what it should look like

### Required to install

* JDK 8
* PostgreSQL

### Environment

environmental variables

```properties
WIP
```

### Clone

- Clone this repo to your local machine using `https://github.com/ita-social-projects/TeachUA`

### Setup

WIP

### How to run local

WIP

---

## Usage

### How to work with swagger UI

WIP

### How to run tests

- To tun unit and integration test use `mvn clean verify -D"checkstyle.skip"=true`
- To run unit tests use `mvn test -D"checkstyle.skip"=true`
- To run integration tests use
    - `mvn failsafe:integration-test`
    - `mvn clean verify -DskipUnitTests -D"checkstyle.skip"=true`

Parameters to skip test:

- `-DskipTests`
- `-DskipUnitTests`
- `-DskipIntegrationTests`

### How to Checkstyle

- To run checkstyle use `mvn validate`

To skip checkstyle use

- PowerShell `-D"checkstyle.skip"=true`
- cmd `-Dcheckstyle.skip`

### Setup Checkstyle

[You can read how to set up Checkstyle here](https://github.com/ita-social-projects/TeachUA/wiki/Tech-manuals#checkstyle-configuration-for-intellij-idea)

---

## Documentation

---

## Contributing

### Git flow

> To get started...

#### Step 1

WIP

### Issue flow

---

## FAQ

WIP

---

## License

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

- **[MIT license](http://opensource.org/licenses/mit-license.php)**
- Copyright 2020 © <a href="https://softserve.academy/" target="_blank"> SoftServe IT Academy</a>.
