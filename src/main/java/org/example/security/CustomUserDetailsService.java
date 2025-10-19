package org.example.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.NguoiDung;
import org.example.repository.NguoiDungRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final NguoiDungRepository nguoiDungRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add role authority if exists
        if (nguoiDung.getVaiTro() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + nguoiDung.getVaiTro().getMa().toUpperCase()));
        } else {
            // Default role if no role assigned
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return User.builder()
                .username(nguoiDung.getTenDangNhap())
                .password(nguoiDung.getMatKhau())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled("INACTIVE".equals(nguoiDung.getTrangThai()))
                .build();
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (nguoiDung.getVaiTro() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + nguoiDung.getVaiTro().getMa().toUpperCase()));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return User.builder()
                .username(nguoiDung.getTenDangNhap())
                .password(nguoiDung.getMatKhau())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled("INACTIVE".equals(nguoiDung.getTrangThai()))
                .build();
    }
}
