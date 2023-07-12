package com.selina.lending.service.quickquote;

import com.selina.lending.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartnerServiceImplTest {

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private PartnerServiceImpl partnerService;

    @Test
    void shouldReturnPartnerObjectIfPartnerExistsInToken() {
        // Given
        when(tokenService.retrievePartnerAccountId()).thenReturn("partner");

        // When
        var result = partnerService.getPartnerFromToken();

        // Then
        assertNotNull(result);
        assertThat(result.getCompanyId(), equalTo("partner"));
        assertThat(result.getSubUnitId(), equalTo("partner"));
    }

    @Test
    void shouldReturnNullIfPartnerNoExistsInToken() {
        // Given
        when(tokenService.retrievePartnerAccountId()).thenReturn(null);

        // When
        var result = partnerService.getPartnerFromToken();

        // Then
        assertNull(result);
    }

}