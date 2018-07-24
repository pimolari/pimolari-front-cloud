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
	
	var _currentProject = HeaderService.getCurrentProject();
	var _incomingProject = null;
	//var __currentProjectId = null;
	
	
	
	if (_currentProject != null) {
		//__currentProjectId = _currentProject.id;
		
		if (_projectId != null) {
			_incomingProject = HeaderService.getProject(_projectId);
			if (_currentProject.id != _incomingProject.id) {
				_currentProject = HeaderService.setCurrentProject(_incomingProject);
				$rootScope.$broadcast('currentProject_change');
			}
		} 
		
		$scope.project = _currentProject;
	} else if (_projectId != null) {
		_currentProject = HeaderService.setCurrentProjectById(_projectId);
		$rootScope.$broadcast('currentProject_change');
		$scope.project = _currentProject;
	} else {
		console.log("  Monitor can not retrieve currentProject and provided project id does not exist");
	}
	
	/*if (_projectId != null) {
		console.log("  Monitor is asked for project id <" + _projectId + ">");
		
		__currentProjectId = _currentProject.id;
		_currentProject = HeaderService.setCurrentProjectById(_projectId);
		
		if (_currentProject != null) {
			$scope.project = _currentProject;
			if (__currentProjectId != null && __currentProjectId != "" && __currentProjectId != _currentProject.id) {
				$rootScope.$broadcast('currentProject_change');
			} 
		} else {
			console.log("  Monitor did not find a project with id <" + _projectId + ">");
		}
	} else {
		console.log("  Monitor simply retrieves current project <" + _currentProject.name + ">");
		_currentProject = HeaderService.getCurrentProject();
		$scope.project = _currentProject;
	}*/
	
	$scope.$on('projectList_change', function() {
		_currentProject = HeaderService.getCurrentProject("monitor projectList_change event");
		console.log("  Monitor updates current project after project list");
		$scope.project = _currentProject;
	});
	
	$scope.$on('currentProject_change', function() {
		_currentProject = HeaderService.getCurrentProject("monitor currentProject_change event");
		if (_projectId != null && _projectId != _currentProject.id) {
			_currentProject = HeaderService.setCurrentProjectById(_projectId);
			$rootScope.$broadcast('currentProject_change');
		} 		
		_projectId = _currentProject.id;
		$scope.project = _currentProject;
	});
			
	$scope.lastupdate = "" + new Date().getTime();
	
	// Scope functions
	// getReadings is used in monitor to retrieve previous device readings when manually requested by user
	// To avoid over requesting, a timestamp check between lastupdate and lastRead.received  is performed before requesting data to server.
	$scope.getReadings = function(device) {
				
		if (device.readings != null && device.readings.length > 0 && device.readings[0] != null) {
					
			console.log("  Monitor says device id " + device.id + " readings have been retrieved at some point");
					
			var check = false;
			var lastUpdateTime = parseInt($scope.lastupdate);
			var lastReadingTime = parseInt(device.lastRead.received);
			var previousReadingTime = parseInt(device.readings[0].received);
					
			if (((lastReadingTime - previousReadingTime) >= 0 )) {
				console.log("  Monitor must retrieve readings for device " + device.id);
				check = true;
			}
					
		} else {
			console.log("  Monitor says device id " + device.id + " readings have not been retrieved yet");
			check = true;
		}
				
				
		if (check) {
			DeviceService.getReadings(device.id, 0, 10)
			.then(function(data) { 
				device.readings = data;
				device.lastChecked = new Date().getTime();
				
			}).catch(function(data) {
				console.log(". Monitor says that an error occurred when getting device " + device.id + " readings " + data);
			});
		}
	}
			
	// Initialize the Timer to run every 10 secs
	// Timer performs the same project structura request 
	$scope.timer = $interval(function () {
		
		console.log("** Monitor timer logic executing for project id " + _projectId);
				
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
										console.log("  Monitor updates device " + device.id + " with data " + device.lastRead.data + " and params " + device.lastRead.params);
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
					console.log("  Monitor says that an error occurred when getting monitor data " + data);
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
				console.log("  Monitor says project with no devices does not need to get refreshed");
			}
		}
				
	}, 300000);
	
	$scope.newDevice = function() {
		console.log("  Monitor navigates to new device");
		$location.path('/device');
	}
	
	$scope.deviceDetails = function(device) {
		console.log("  Monitor navigates to device " + device.id + " details");
		DeviceService.setCurrentDevice(device);
		$location.path('/device/' + device.id);
	}
	
	$scope.navigate = function(path) {
		console.log("  Monitor navigates to " + path);
		$location.path(path);
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

