package com.myanime.infrastructure.jparepos.jpa;

import com.myanime.infrastructure.entities.jpa.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, String> {
}
