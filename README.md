# Lhotse security module
[![Build Status](https://travis-ci.com/everest-engineering/lhotse-security.svg?branch=master)](https://travis-ci.com/everest-engineering/lhotse-security) ![Latest release](https://img.shields.io/github/v/release/everest-engineering/lhotse-security)

This is a supporting repository for [Lhotse](https://github.com/everest-engineering/lhotse), a starter kit for writing event sourced web applications following domain driven design principles.

The `security` module builds on 
[Spring Security OAuth](https://projects.spring.io/spring-security-oauth/docs/oauth2.html).
Out of the box, it sets up both an __authorization server__ and a __resource server__ (the main application) that 
facilitate an authentication and authorisation workflow based on [OAuth2](https://oauth.net/2/). Stateless sessions 
using [Jason Web Tokens](https://jwt.io/) (JWT) makes is easy to extract microservices.

JWT tokens are issued by the authorization server which client applications include as part of the `Authorization`
header included with every API request. The main application -- the resource server in OAuth parlance -- uses a shared
secret to validate each request and enforces role based authorisation.

Our initial set up has both authorisation and resource servers running together in a single application.  A single 
hard-coded client, `web-app-ui`, is configured in the authorisation server to support the 
[password grant](https://oauth.net/2/grant-types/password/) approach to exchanging credentials. Front end applications 
need to specify this identify to perform authentication & authorisation on behalf of end users.

If necessary, the authorisation server can be extracted into its own service to serve multiple resource servers. Third
party OAuth2 providers can also be integrated with the resource server.

## Using the module
You need to write service components that implement the interfaces `AuthenticationServerUserDetailsService` 
and `ApplicationUserDetailsService`. The implementations are used by the security module so that the module needn't be
aware of how the users are actually stored or what attributes they contain.


## License
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

[![License: EverestEngineering](https://img.shields.io/badge/Copyright%20%C2%A9-EVERESTENGINEERING-blue)](https://everest.engineering)

>Talk to us `hi@everest.engineering`.

