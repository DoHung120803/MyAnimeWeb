package com.myanime.repository.jpa;

import com.myanime.entity.jpa.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, String> {
}
