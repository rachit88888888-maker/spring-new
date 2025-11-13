package com.example.controller;

import javax.servlet.http.HttpServletRequest;

import com.example.azure.AzureIdTokenValidator;
import com.example.azure.AzureUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AzureCallbackController {

    private static final String ISSUER = "https://sts.windows.net/common/";
    private static final String AUDIENCE = "00000000-0000-0000-0000-000000000000"; // TODO replace with your app ID

    @Autowired
    private AzureIdTokenValidator tokenValidator;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/login/azure/callback", method = RequestMethod.POST)
    public ModelAndView handlePost(@RequestParam(value = "id_token", required = false) String idToken,
                                   @RequestParam(value = "code", required = false) String code,
                                   HttpServletRequest request) {
        return process(idToken, code, request);
    }

    @RequestMapping(value = "/login/azure/callback", method = RequestMethod.GET)
    public ModelAndView handleGet(@RequestParam(value = "id_token", required = false) String idToken,
                                  @RequestParam(value = "code", required = false) String code,
                                  HttpServletRequest request) {
        return process(idToken, code, request);
    }

    private ModelAndView process(String idToken, String code, HttpServletRequest request) {
        if (idToken == null && code != null) {
            // The IdP-initiated flow might send only a code; this sample expects an id_token.
            return new ModelAndView("login").addObject("error", "id_token missing");
        }
        if (idToken == null) {
            return new ModelAndView("login").addObject("error", "No token received");
        }

        // Azure initiated the callback, so state/nonce cannot be validated because we never generated them.
        // We rely on issuer, audience, expiration, and signature checks instead.
        AzureUserInfo userInfo = tokenValidator.validate(idToken, ISSUER, AUDIENCE);

        PreAuthenticatedAuthenticationToken authenticationRequest =
                new PreAuthenticatedAuthenticationToken(userInfo.getUsername(), "N/A");
        authenticationRequest.setDetails(userInfo);
        try {
            Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
            SecurityContextHolder.getContext().setAuthentication(authenticationResult);
            // SecurityContextPersistenceFilter sees the populated context and saves it in the HTTP session automatically.
            return new ModelAndView(new RedirectView(request.getContextPath() + "/home", true));
        } catch (AuthenticationException ex) {
            return new ModelAndView("login").addObject("error", ex.getMessage());
        }
    }
}
