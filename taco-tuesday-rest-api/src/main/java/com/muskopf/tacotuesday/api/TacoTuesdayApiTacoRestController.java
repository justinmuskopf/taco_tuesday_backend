package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.bl.proc.TacoEmailer;
import com.muskopf.tacotuesday.bl.proc.TacoTuesdayResourceMapper;
import com.muskopf.tacotuesday.domain.Taco;
import com.muskopf.tacotuesday.domain.TacoPriceList;
import com.muskopf.tacotuesday.resource.TacoResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/taco-tuesday/v1/tacos")
public class TacoTuesdayApiTacoRestController {
    private Logger logger = LoggerFactory.getLogger(TacoTuesdayApiTacoRestController.class);
    private TacoPriceList tacoPriceList;
    private TacoTuesdayResourceMapper mapper;
    private TacoEmailer emailer;

    @Autowired
    public TacoTuesdayApiTacoRestController(TacoPriceList tacoPriceList, TacoTuesdayResourceMapper mapper, TacoEmailer emailer) {
        this.tacoPriceList = tacoPriceList;
        this.mapper = mapper;
        this.emailer = emailer;
    }

    @GetMapping
    public ResponseEntity<List<TacoResource>> getTacoPrices() {
        List<Taco> tacos = tacoPriceList.getPriceList();

        return new ResponseEntity<>(mapper.mapToTacoResources(tacos), HttpStatus.OK);
    }
}
