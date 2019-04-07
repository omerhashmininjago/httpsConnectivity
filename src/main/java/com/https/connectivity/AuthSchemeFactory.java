package com.https.connectivity;

import com.google.common.collect.ImmutableMap;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.impl.auth.*;

public class AuthSchemeFactory {

    private static ImmutableMap<String, AuthSchemeProvider> authSchemeProviderMap;

    static {
        authSchemeProviderMap = ImmutableMap.of(
                AuthSchemes.BASIC, new BasicSchemeFactory(),
                AuthSchemes.DIGEST, new DigestSchemeFactory(),
                AuthSchemes.KERBEROS, new KerberosSchemeFactory(),
                AuthSchemes.NTLM, new NTLMSchemeFactory(),
                AuthSchemes.SPNEGO, new SPNegoSchemeFactory());
    }

    public static AuthSchemeProvider getAuthSchemeProvider(String authScheme) {

        if (authSchemeProviderMap.containsKey(authScheme)) {
            return authSchemeProviderMap.get(authScheme);
        }
        throw new RuntimeException("Illegal AuthScheme");
    }
}
