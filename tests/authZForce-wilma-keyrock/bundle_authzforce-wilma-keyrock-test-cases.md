# Bundle of GEs Wilma-AuthZForce-Keyrock test cases description #

The purpose of this document is to describe the performance test case related to the integration of the follwowing three Generic Enablers: **IdM**, **PEP Proxy** and **PDP**, more specifically their reference implementation named respectively, **KeyRock**, **Wilma** and **AuthZForce**.
For the preparation of this test, the most used methods for protecting access to a configured IdM/PDP application resource have been taken into account;  in fact the test involves the `resource access` API for managing access to protected REST services.

The picture below, that is taken from the GE official documentation, shows how the GEs KeyRock, Wilma and AuthZForce can be used in FIWARE and how they fit with the FIWARE architecture.

![Update data flow](./680px-AuthorizationPDP.Architecture.FMC.Block.png)

##Policy decision point test case ##

This test aims to stress the `resource access` API in order to evaluate performance of authorization related to a given access request; therefore a valid `access token` (already configured in AuthZForce) is needed to complete this operation.

The main actors involved by this scenario, which are among those showed previously in the general structure of the FIWARE GE, are:

- `Access Requester`
- `PEP Proxy GE`
- `IdM GE`
- `PDP GE`
- `Protected REST Service`

The actor `Access Requester` is played by the **JMeter** application that is installed on a dedicated server and configured to send a `rosource access` request to the PEP Proxy GE **Wilma**.

The actors `PEP Proxy GE`, `IdM GE` and `PDP GE` are played by the related reference implementations which are, respectively: **Wilma**, **Keyrock** and **AuthZForce**; they are installed each one in a dedicated server and they are in charge of fulfilling the aforementioned request.

The actor `Protected REST Service` is played by the **Apache** application installed on a dedicated server and it is configured to receive a `REST` request from **Wilma**.

The scenario is realised by executing the following main steps:

1. JMeter sends a `resource access` request to Wilma.
2. Wilma, once received the request, sends a first `authorization request` to Keyrock.
3. Keyrock, once verified the incoming request, sends a positive (HTTP code 200) or negative response (HTTP code 401) to Wilma, as the case.
4. In case of positive response by Keyrock, Wilma sends a second `authorization request` to AuthZForce.
5. AuthZForce, once verified the incoming request, sends a positive (HTTP code 200) or negative response (HTTP code 401) to Wilma, as the case.
6. In case of positive response by AuthZForce, Wilma sends the configured `REST` request to Apache and provides the Apache response to JMeter.

**Attention point:**
> In order to speed up the process,  Wilma saves to a cache the couple `token`-`keyrock response`  received by KeyRock at step 4 for a configurable period set by default to 300 s; in this way, for each subsequent token already checked by KeyRock, steps 2 and 3 are skipped going directly to the step 4.

> Since in this test, for simplicity needs, only one token is used (and reused many times during the test), this caching mechanism should be disabled in order to produce the same behavior (and response times) we would have with different tokens. How to configure this setting is detailed in the [related test guide line](../../doc/manuals/authZForce-wilma-keyrock/bundle_authzforce-wilma-keyrock-test-guide-lines.md).

For this test JMeter has to be configured with a growing number of threads (max 50) which continuosly solicit the `resource access` API for a period of 30 minutes.

An example of the payload contained by these requests can be found in the table below:
![Update data flow](./Table1.png)

