# Spring Security Demo Project

This is a reference and test project for myself and colleagues for Spring Security implementations with the latest Spring Boot libraries.

## What does this project do?

* Enables Spring Security's default form and basic auth login, but allowing POST requests at the same time so that data can be saved via `fetch` (currently this is done disabling csrf, cross-origin headers would be more secure)
* A complete unit test testing the security behaviour with a web environment and `@SpringBootTest`. (see `SpringSecurityHttpTest`)
* A profile `no-security` which is disabling Spring Security

## ToDo

* Different roles for UI and api
* Redirect all non-api requests to `index.html` so that a frontend framework can be used for routing
* Add a profile that is managing users in a provided SQLite database with Spring's data source support
* Custom login form
* Create a working version without disabling CSRF completely
* Use/evaluate Java's new `Record` for data classes

