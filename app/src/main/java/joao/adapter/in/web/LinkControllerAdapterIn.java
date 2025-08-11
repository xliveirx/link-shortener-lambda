package joao.adapter.in.web;

import jakarta.validation.Valid;
import joao.adapter.in.web.dto.ShortenLinkRequest;
import joao.adapter.in.web.dto.ShortenLinkResponse;
import joao.core.domain.User;
import joao.core.port.in.RedirectPortIn;
import joao.core.port.in.ShortenLinkPortIn;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
public class LinkControllerAdapterIn {

    private final ShortenLinkPortIn shortenLinkPortIn;
    private final RedirectPortIn redirectPortIn;

    public LinkControllerAdapterIn(ShortenLinkPortIn shortenLinkPortIn, RedirectPortIn redirectPortIn) {
        this.shortenLinkPortIn = shortenLinkPortIn;
        this.redirectPortIn = redirectPortIn;
    }

    @PostMapping("/links")
    public ResponseEntity<ShortenLinkResponse> shortenLink(@RequestBody @Valid ShortenLinkRequest req,
                                                           @AuthenticationPrincipal User user) {

        var userId = UUID.fromString(user.getId());

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
}
