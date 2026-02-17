package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.ProfileImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImgRepository extends JpaRepository<ProfileImg, Long> {

    ProfileImg findByImgUrl(String profileImg);
}
