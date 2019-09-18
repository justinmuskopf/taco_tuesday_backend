package com.respec.tacotuesday.domain;

import com.respec.tacotuesday.config.TacoTuesdayConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public enum TacoType {
    BARBACOA,
    BEEF_FAJITA,
    CABEZA,
    CARNITAS,
    CHICKEN_FAJITA,
    LENGUA,
    PASTOR,
    TRIPA;
}
