package joao.adapter.in.web;

import jakarta.validation.Valid;
import joao.adapter.in.web.dto.*;
import joao.core.domain.LinkFilter;
import joao.core.domain.User;
import joao.core.port.in.AnalyticsPortIn;
import joao.core.port.in.MyLinksPortIn;
import joao.core.port.in.RedirectPortIn;
import joao.core.port.in.ShortenLinkPortIn;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping
public class LinkControllerAdapterIn {

    private final ShortenLinkPortIn shortenLinkPortIn;
    private final RedirectPortIn redirectPortIn;
    private final MyLinksPortIn myLinksPortIn;
    private final AnalyticsPortIn analyticsPortIn;

    public LinkControllerAdapterIn(ShortenLinkPortIn shortenLinkPortIn, RedirectPortIn redirectPortIn, MyLinksPortIn myLinksPortIn, AnalyticsPortIn analyticsPortIn) {
        this.shortenLinkPortIn = shortenLinkPortIn;
        this.redirectPortIn = redirectPortIn;
        this.myLinksPortIn = myLinksPortIn;
        this.analyticsPortIn = analyticsPortIn;
    }

    @PostMapping("/links")
    public ResponseEntity<ShortenLinkResponse> shortenLink(@RequestBody @Valid ShortenLinkRequest req,
                                                           @AuthenticationPrincipal User user) {

        var userId = user.getUserId();

        var res = shortenLinkPortIn.execute(req.toDomain(userId));

        var uri = URI.create("/");

        return ResponseEntity.created(uri).body(res);
    }

    @GetMapping("/r/{linkId}")
    public ResponseEntity<ShortenLinkResponse> redirect(@PathVariable String linkId) {

        var fullUrl = redirectPortIn.execute(linkId);

        HttpHeaders headers = new HttpHeaders();

        headers.setLocation(URI.create(fullUrl));

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    @GetMapping("/links")
    public ResponseEntity<ApiResponse<LinkResponse>> userLinks(@RequestParam(name = "nextToken", defaultValue = "") String nextToken,
                                                               @RequestParam(name = "limit", defaultValue = "3") Integer limit,
                                                               @RequestParam(name = "active", required = false) Boolean active,
                                                               @RequestParam(name = "startCreatedAt", required = false) LocalDate startCreatedAt,
                                                               @RequestParam(name = "endCreatedAt", required = false) LocalDate endCreatedAt,
                                                               @AuthenticationPrincipal User user) {

        var userId = String.valueOf(user.getUserId());

        var response = myLinksPortIn.execute(userId, nextToken, limit, new LinkFilter(active, startCreatedAt, endCreatedAt));

        return ResponseEntity.ok(
                new ApiResponse<>(
                        response.items().stream().map(LinkResponse::fromDomain).toList(),
                        response.nextToken()

                )
        );
    }

    @GetMapping("/links/{linkId}/analytics")
    public ResponseEntity<AnalyticsResponse> linkAnalytics(@PathVariable("linkId") String linkId,
                                                               @RequestParam(name = "startDate") LocalDate startDate,
                                                               @RequestParam(name = "endDate") LocalDate endDate,
                                                               @AuthenticationPrincipal User user) {
        var userId = String.valueOf(user.getUserId());

        var body = analyticsPortIn.execute(userId, linkId, startDate, endDate);

        return ResponseEntity.ok(body);

    }

}
