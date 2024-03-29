package com.selina.lending.service.quickquote;

import com.selina.lending.httpclient.middleware.dto.qq.request.Partner;
import com.selina.lending.service.TokenService;
import org.springframework.stereotype.Service;

@Service
public class PartnerServiceImpl implements PartnerService{

    private final TokenService tokenService;

    public PartnerServiceImpl(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Partner getPartnerFromToken() {
        var partnerAccountId = tokenService.retrievePartnerAccountId();
        if (partnerAccountId == null) {
            return null;
        }
        return Partner.builder().companyId(partnerAccountId).subUnitId(partnerAccountId).build();
    }
}
