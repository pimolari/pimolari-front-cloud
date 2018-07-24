//(function() {
var devices = [];
	
angular.module("ew.monitor")
	
.run(function(HeaderService, ProjectService) {
	// This is effectively part of the main method initialization code
	console.log("Monitor Module initialised");
})

.controller("MonitorController", function($scope, $rootScope, $location, $routeParams, $interval, HeaderService, ProjectService, DeviceService) { 
	console.log("Monitor Controller executing...");
			
	var _projectId = $routeParams.projectId;
	//var _projects = HeaderService.getProjectList();
	var _currentProject = null;
	
	if (_projectId != null) {
		_currentProject = HeaderService.setCurrentProject(_projectId);
		
		if (_currentProject != null) {
			$scope.project = _currentProject;
			$rootScope.$broadcast('currentProjectChange');
		}
	} 
	
	$scope.$on('ProjectListChange', function() {
		//_projects = HeaderService.getProjectList();
		_currentProject = HeaderService.getCurrentProject("monitor ProjectListChange event");
		console.log("  Event ProjectListChange received in monitor " + _currentProject.name);
		_projectId = _currentProject.id;
		$scope.project = _currentProject;
		// ??? $rootScope.$broadcast('currentProjectChange');
	});
			
	$scope.lastupdate = "" + new Date().getTime();
	
	// Scope functions
	// getReadings is used in monitor to retrieve previous device readings when manually requested by user
	// To avoid over requesting, a timestamp check between lastupdate and lastRead.received  is performed before requesting data to server.
	$scope.getReadings = function(device) {
				
		if (device.readings != null && device.readings.length > 0 && device.readings[0] != null) {
					
			console.log("This device's readings have been retrieved at some point");
					
			var check = false;
			var lastUpdateTime = parseInt($scope.lastupdate);
			
			var lastReadingTime = parseInt(device.lastRead.received);
			var previousReadingTime = parseInt(device.readings[0].received);
					
			//console.log("Last reading at " + lastReadingTime + " previous reading at " + previousReadingTime);	
					
			if (((lastReadingTime - previousReadingTime) >= 0 )) {
				console.log("Must retrieve readings...");
				check = true;
			}
					
		} else {
			console.log("This device readings have not been retrieved yet");
			check = true;
		}
				
				
		if (check) {
			DeviceService.getReadings(device.id, 0, 10)
			.then(function(data) { 
				device.readings = data;
				device.lastChecked = new Date().getTime();
				//console.log("  Readings retrieved for device " + device.id + " :: " + device.readings.length );
				
				// TO REMOVE
				/*for (var i = 0; device.readings.length; i++) {
					if (device.readings[i].params != null) {
						for (var j = 0; device.readings[i].params.length; j++) {
								console.log("      > " + device.readings[i].params[i].key + ":" + device.readings[i].params[i].value);
						}
					}
				}*/
				
			}).catch(function(data) {
				console.log("Error getting the data " + data);
			});
		}
	}
			
	// Initialize the Timer to run every 10 secs
	// Timer performs the same project structura request 
	$scope.timer = $interval(function () {
		
		//var _project = HeaderService.getProject();
		
		console.log("** Timer logic executing for project id " + _projectId);
				
		if (_currentProject != null) {
					
			if (_currentProject.devices.length > 0) {
				// Updates last update time with current value
				$scope.lastupdate = "" + new Date().getTime();
					
				ProjectService.monitor(_projectId)
				.then(function(data) { 
					
					if (data != null) {
						var devices = data;
					
						for (var i = 0; i < devices.length; i++) {
							var updated = false;
							var device = devices[i];
						
							for (var j = 0; j < $scope.project.devices.length; j++) {
								if (device.id === $scope.project.devices[j].id) {
									if (device.lastRead != null) {
										console.log("  Updates device " + device.id + " with data " + device.lastRead.data + " and params " + device.lastRead.params);
										$scope.project.devices[j].lastRead = device.lastRead;
									}
									
									updated = true;
								
									break;
								}

							}

							if (!updated) {
								$scope.project.devices.push(device);
							}
						}
					}
				}).catch(function(data) {
					console.log("Error getting the data " + data);
				});	
					
				/*
				ProjectService.get(_projectId)
				.then(function(data) { 
					
					var project = data;

					for (var i = 0; i < project.devices.length; i++) {
						var updated = false;
						var device = project.devices[i];
						
						for (var j = 0; j < $scope.project.devices.length; j++) {
							if (device.id === $scope.project.devices[j].id) {
								if (device.lastRead != null) {
									console.log("  Updates device " + device.id + " with data " + device.lastRead.data + " and params " + device.lastRead.params);
									$scope.project.devices[j].lastRead = device.lastRead;
								}
									
								updated = true;
								
								break;
							}

						}

						if (!updated) {
							$scope.project.devices.push(device);
						}
						
					}
					
				}).catch(function(data) {
					console.log("Error getting the data " + data);
				});
				*/
			} else {
				console.log("  Project with no devices does not need to get refreshed");
			}
		}
				
	}, 300000);
	
	$scope.newDevice = function() {
		console.log("Navigate to new device");
		$location.path('/device');
	}
	
	$scope.deviceDetails = function(device) {
		console.log("Navigate to device details");
		DeviceService.setCurrentDevice(device);
		$location.path('/device/' + device.id);
	}
	
	/** Consider removing */
	$scope.getDeviceParamValue = function(index, device, key) {
		//console.log(" PARAM VALUE " + index + ": " + device.name + "  : " + key);
		var lastRead = device.lastRead;
		
		if (lastRead != null && lastRead.params != null) {
			
			for (var i = 0; i < lastRead.params.length; i++) {
				if (lastRead.params[i].key === key) {
					//console.log("   RETURNS " + lastRead.params[i].value);
					return lastRead.params[i].value;
				}
			}
			
		} else {
			return null;
		}
	}
			
});
		
		

		

