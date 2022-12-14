package com.example.demo.rest;

import com.example.demo.dto.HitDto;
import com.example.demo.dto.request.HitRequest;
import com.example.demo.security.provider.JwtAuthenticationToken;
import com.example.demo.security.userDetails.CustomUserDetails;
import com.example.demo.service.HitService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hits")
public class HitController {
    private final HitService hitService;
    private final UserService userService;


    @GetMapping("/all/")
    public ResponseEntity<?> getAllHits(JwtAuthenticationToken authentication) {
        CustomUserDetails details = authentication.getDetails();
        long id = details.getId();
        List<HitDto> allByUserId = hitService.getAllByUserId(id);
        return ResponseEntity.ok(allByUserId);
    }


    @GetMapping("/{hitId}")
    public ResponseEntity<?> getHit(JwtAuthenticationToken authentication, @PathVariable Long hitId){
        CustomUserDetails details = authentication.getDetails();
        long id = details.getId();
        HitDto hitDto = hitService.findById(hitId);
        //if (hitDto.getId()==id){
            return ResponseEntity.ok(hitDto);
        //}
        //return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/")
    public ResponseEntity<?> saveHit(JwtAuthenticationToken authentication, @Validated @RequestBody HitRequest hitRequest){
        long time = System.nanoTime();
        CustomUserDetails details = authentication.getDetails();
        long id = details.getId();
        HitDto hit = hitService.save(hitRequest, id, time, LocalDateTime.now(ZoneOffset.UTC));
        return ResponseEntity.ok(hit);
    }
}


