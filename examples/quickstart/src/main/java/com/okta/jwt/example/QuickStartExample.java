/*
 * Copyright 2017 Okta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.okta.jwt.example;

import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.IdTokenVerifier;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerifiers;

import java.time.Duration;

public class QuickStartExample {

    public static void main(String[] args) throws Exception {

        if (args == null || args.length != 3) {
            System.err.println("Usage: "+ QuickStartExample.class.getName() +" [issuerUrl] [audience] [jwtAccessToken]");
            System.exit(1);
        }

        String issuerUrl = args[0];
        String audience  = args[1];
        String jwtString = args[2];
        String clientId  = args[3];
        String idTokenString = args[4];
        String nonce = args[5];


        // 1. build the parser
        AccessTokenVerifier jwtVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                                    .setIssuer(issuerUrl)
                                    .setAudience(audience)
                                    .setConnectionTimeout(Duration.ofSeconds(1)) // defaults to 1000ms
                                    .build();

        // 2. Process the token (includes validation)
        Jwt jwt = jwtVerifier.decode(jwtString);

        // 3. Do something with the token
        System.out.println(jwt.getTokenValue()); // print the token
        System.out.println(jwt.getClaims().get("invalidKey")); // an invalid key just returns null
        System.out.println(jwt.getClaims().get("groups")); // handle an array value
        System.out.println(jwt.getExpiresAt()); // print the expiration time
        
        
        // To decode IdToken - nonce is required. It will be available as a cookie if you have used Okta SignIn Widget.
        // For others, please check the nonce value sent in request.
         IdTokenVerifier idTokenVerifier = JwtVerifiers.idTokenVerifierBuilder()
        		.setIssuer(issuerUrl)
        		.setClientId(clientId)
        		.setConnectionTimeout(Duration.ofSeconds(1))
        		.build();
        
        Jwt idJwt = idTokenVerifier.decode(idTokenString, nonce);
        	
        System.out.println(idJwt.getTokenValue()); // print the token
        System.out.println(idJwt.getClaims().get("invalidKey")); // an invalid key just returns null
        System.out.println(idJwt.getClaims().get("nonce").equals(nonce));
        System.out.println(idJwt.getExpiresAt()); // print the expiration time
        
  
        
    }
}
