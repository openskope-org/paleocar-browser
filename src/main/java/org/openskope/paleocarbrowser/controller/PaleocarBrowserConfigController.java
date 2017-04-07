package org.openskope.paleocarbrowser.controller;

import org.openskope.paleocarbrowser.model.PaleocarBrowserConfig;
import org.openskope.paleocarbrowser.service.PaleocarBrowserConfigService;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController 
@EnableAutoConfiguration 
@RequestMapping("${paleocar-browser.config-endpoint}")
public class PaleocarBrowserConfigController {

	private final PaleocarBrowserConfigService configService;

	@Autowired
	public PaleocarBrowserConfigController(PaleocarBrowserConfigService configService) {
		this.configService = configService;
	}

	@RequestMapping(value="config", method=RequestMethod.GET)
	public PaleocarBrowserConfig getConfig() {
		return configService.getConfig();
	}
}