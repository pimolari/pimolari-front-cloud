
	
  var environment = "http://localhost:8080/";
  //var environment = "http://www.pimolari.com/";

	angular.module("ew.device.service", []); // war/components/device/device-service.js
	angular.module("ew.project.service", ["ew.device.service"]); // war/components/project/project-service.js		
	angular.module("ew.monitor.service", []); // war/components/monitor/monitor-service.js
	angular.module("ew.header.service", []); // war/components/menu/menu-service.js
	
	angular.module("ew.device", ["ew.header.service", "ew.device.service"]); //
	angular.module("ew.project", ["ew.header.service", "ew.device.service"]); //
	angular.module("ew.monitor", ["ew.monitor.service", "ew.project.service", "ew.header.service", "ew.device.service"]); // war/components/monitor/monitor.js	
	angular.module("ew.header", ["ew.header.service", "ew.project.service"]); // war/components/header/header.js
	
	angular.module("ew", ['ngRoute', 'ew.header', 'ew.monitor', 'ew.project', 'ew.device']);
	
	console.log("app loaded");