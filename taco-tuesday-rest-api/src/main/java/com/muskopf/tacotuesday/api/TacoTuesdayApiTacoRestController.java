package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.bl.proc.TacoEmailer;
import com.muskopf.tacotuesday.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/taco-tuesday/v1/tacos")
public class TacoTuesdayApiTacoRestController {
    private Logger logger = LoggerFactory.getLogger(TacoTuesdayApiTacoRestController.class);
    private TacoPriceList tacoPriceList;
    private TacoEmailer emailer;

    @Autowired
    public TacoTuesdayApiTacoRestController(TacoPriceList tacoPriceList, TacoEmailer emailer) {
        this.tacoPriceList = tacoPriceList;
        this.emailer = emailer;
    }

    @GetMapping
    public ResponseEntity<List<Taco>> getTacoPrices() {
        return new ResponseEntity<>(tacoPriceList.getPriceList(), HttpStatus.OK);
    }
}
