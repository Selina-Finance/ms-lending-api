package com.selina.lending.internal.service.quickquote;

import com.selina.lending.httpclient.middleware.dto.qq.request.Partner;

public interface PartnerService {
    Partner getPartnerFromToken();
}
