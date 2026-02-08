package com.myanime.infrastructure.jparepos;

import com.myanime.infrastructure.entities.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, String> {
}
