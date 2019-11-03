package hack.ohio.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hack.ohio.dashboard.service.CSVService;

@RequestMapping("/")
@RestController
public class CSVController {
	
	@Autowired
  	private CSVService csv_service;

	@RequestMapping(value = "init", method = RequestMethod.GET)
	public ResponseEntity<Object> doAtStart()
	{
		try
		{
			csv_service.initialize();
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error.\n" + e.getMessage());
		}
	}

	@RequestMapping(value = "date/", method = RequestMethod.GET)
	public ResponseEntity<Object> getDateRange(@RequestParam(value="start", required=true) final String start, @RequestParam(value="end", required=true) final String end)
	{
		try
		{
			if (start.equals(end))
			{
				return new ResponseEntity<>(CSVService.getPowersDay(start), HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>(CSVService.getPowersRange(start, end), HttpStatus.OK);
			}
		}
		catch (Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error.\n" + e.getMessage());
		}
	}

	@RequestMapping(value = "monthBeforeSum/", method = RequestMethod.GET)
	public ResponseEntity<Object> getMonthBeforeSum(@RequestParam(value="start", required=true) final String start)
	{
		try
		{
			return new ResponseEntity<>(CSVService.getPastMonthSum(start), HttpStatus.OK);
		}
		catch (Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error.\n" + e.getMessage());
		}
	}

	@RequestMapping(value = "weekBeforeData/", method = RequestMethod.GET)
	public ResponseEntity<Object> getWeekBeforeData(@RequestParam(value="start", required=true) final String start)
	{
		try
		{
			return new ResponseEntity<>(CSVService.getPastWeekPower(start), HttpStatus.OK);
		}
		catch (Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error.\n" + e.getMessage());
		}
	}
	@RequestMapping(value = "weekBeforeDate/", method = RequestMethod.GET)
	public ResponseEntity<Object> getWeekBeforeDate(@RequestParam(value="start", required=true) final String start)
	{
		try
		{
			return new ResponseEntity<>(CSVService.getPastWeekDate(start), HttpStatus.OK);
		}
		catch (Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error.\n" + e.getMessage());
		}
	}

}
