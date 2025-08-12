package joao.adapter.in.web;

import jakarta.validation.Valid;
import joao.adapter.in.web.dto.ApiResponse;
import joao.adapter.in.web.dto.LinkRespose;
import joao.adapter.in.web.dto.ShortenLinkRequest;
import joao.adapter.in.web.dto.ShortenLinkResponse;
import joao.core.domain.Link;
import joao.core.domain.User;
import joao.core.port.in.MyLinksPortIn;
import joao.core.port.in.RedirectPortIn;
import joao.core.port.in.ShortenLinkPortIn;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
public class LinkControllerAdapterIn {

    private final ShortenLinkPortIn shortenLinkPortIn;
    private final RedirectPortIn redirectPortIn;
    private final MyLinksPortIn myLinksPortIn;

    public LinkControllerAdapterIn(ShortenLinkPortIn shortenLinkPortIn, RedirectPortIn redirectPortIn, MyLinksPortIn myLinksPortIn) {
        this.shortenLinkPortIn = shortenLinkPortIn;
        this.redirectPortIn = redirectPortIn;
        this.myLinksPortIn = myLinksPortIn;
    }

    @PostMapping("/links")
    public ResponseEntity<ShortenLinkResponse> shortenLink(@RequestBody @Valid ShortenLinkRequest req,
                                                           @AuthenticationPrincipal User user) {

        var userId = user.getUserId();

        var res = shortenLinkPortIn.execute(req.toDomain(userId));

        var uri = URI.create("/");

        return ResponseEntity.created(uri).body(res);
    }

    @GetMapping("/{linkId}")
    public ResponseEntity<ShortenLinkResponse> redirect(@PathVariable String linkId) {

        var fullUrl = redirectPortIn.execute(linkId);

        HttpHeaders headers = new HttpHeaders();

        headers.setLocation(URI.create(fullUrl));

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    @GetMapping("/links")
    public ResponseEntity<ApiResponse<LinkRespose>> userLinks(@RequestParam(name = "nextToken", defaultValue = "") String nextToken,
                                                              @RequestParam(name = "limit", defaultValue = "3") Integer limit,
                                                              @AuthenticationPrincipal User user) {

        var userId = String.valueOf(user.getUserId());

        var response = myLinksPortIn.execute(userId, nextToken, limit);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        response.items().stream().map(LinkRespose::fromDomain).toList(),
                        response.nextToken()

                )
        );
    }
}
