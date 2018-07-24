//(function() {
var devices = [];
	
angular.module("ew.device")
	
.run(function(HeaderService, DeviceService) {
	// This is effectively part of the main method initialization code
	console.log("Device Module initialised");
})

.controller("DeviceController", function($scope, $rootScope, $location, $routeParams, HeaderService, ProjectService, DeviceService) { 
	console.log("Device Controller executing... GGGG");
	
	$scope.loading = true;
	
	var _update = false;
	var _projects = HeaderService.getProjectList();
	var _currentProject = HeaderService.getCurrentProject();
	console.log("  CURRENT PROJECT RETRIEVED: " + _currentProject);
	
	var _params = [];
	var _deviceId = $routeParams.deviceId;
	console.log("  DEVICE ID RETRIEVED: " + _deviceId);

	var _currentDevice = DeviceService.getCurrentDevice();
	console.log("  CURRENT DEVICE: " + _currentDevice);
	
	if (_currentDevice != null) {

		//console.log("  DEVICE FOUND!!! " + _currentDevice);
		_update = true;
		$scope.loading = false;
		//prepareCharts();
		$scope.device = _currentDevice;

	} else {
		
		_currentDevice = {};
		console.log("  Retrieves device by Id " + (_deviceId != null));

		if (_deviceId != null) {
			DeviceService.get(_deviceId)
		  .then(function(device) { 

				console.log("  Device retrieved with id " + device.id);
				_currentDevice = device;
				//prepareCharts();
				$scope.device = _currentDevice;
			
			}).catch(function(data) {
				console.log("  Error getting project devices data " + data);
			});
		}
	}
	
	/***/
	$scope.$on('currentProjectChange', function() {
		_currentProject = HeaderService.getCurrentProject();
		
		if (_currentDevice != null && _currentDevice.id == null && _deviceId != null) {
			$scope.loading = false;
			//console.log("  WATCH THIS... no currentDevice but incoming deviceId retrieved! Must match on current project ");
			
			for (var i = 0; i < _currentProject.devices.length; i++) {
				if (_currentProject.devices[i].id === _deviceId) {
					_currentDevice = _currentProject.devices[i];
					$scope.device = _currentDevice;
					//console.log("  DEVICE FOUND!!!");
					_update = true;
					break;
				}
			}
		}
		
	});
	
	$scope.__param = {
		value: {
	  	key: null,
			name: null,
			type: "string",
			showLabel: "name",
			showValue: "always",
		}
	}
	
	/** change this to config */
	$scope.config = {
		value: {
	  	key: null,
			name: null,
			type: "string",
			showLabel: "name",
			showValue: "always",
		},
	  options: {
			type: [
				{id: 'string', name: 'String'},
				{id: 'integer', name: 'Integer'},
				{id: 'double', name: 'Double'},
				{id: 'email', name: 'Email'},
			],
	  	label: [
				{id: 'name', name: 'Name'},
				{id: 'key', name: 'Key'},
				{id: 'name-key', name: 'Name(Key)'},
				{id: 'key-name', name: 'Key(Name)'},
				{id: 'none', name: 'None'}
			],
			value: [
				{id: 'always', name: 'Always show value'},
				{id: 'never', name: 'Never show value'},
				{id: 'as-received', name: 'Only if received'}
			]
	  }	
	};
	

	/***/
	$scope.saveDevice = function(device) {
		
		console.log("SAVING DEVICE on project " + _currentProject.id);
		
		if (device != null && _currentProject != null) {
			
			device.projectId = _currentProject.id;
			
			console.log("  Device to save: ");
			console.log("  name: " + device.name);
			console.log("  description: " + device.description);
			console.log("  id: " + device.id);
			console.log("  lastRead: " + device.lastRead);
			console.log("  modified: " + device.modified);
			console.log("  projectId: " + device.projectId);
			console.log("  created: " + device.created);
			console.log("  type: " + device.type);
			console.log("  params: " + device.params);
			
			//device.params = {};
			//device.params.params = params;
			//device.params = params;
			
			if (_update == true) {
				DeviceService.put(device)
				.then(function(data) { 
					device = data;
					console.log("  Device updated " + device.name + " (" + device.id + ")");
				
					ProjectService.list()
				  .then(function(data) { 
			
						console.log("  Retrieved " + data.length + " projects");
						HeaderService.setProjectList(data);

						$rootScope.project = HeaderService.getCurrentProject();
						$rootScope.$broadcast('ProjectListChange');
						DeviceService.removeCurrentDevice();
						$location.path('monitor/' + _currentProject.id);
					
					}).catch(function(data) {
						console.log("  Error getting project devices data " + data);
					});
				
				}).catch(function(data) {
					console.log("  Error saving device " + device.name + " (" + device.id + ")");
				});
			} else {
				DeviceService.post(device)
				.then(function(data) { 
					device = data;
					console.log("  Device saved " + device.name + " (" + device.id + ")");
				
					ProjectService.list()
				  .then(function(data) { 
			
						console.log("  Retrieved " + data.length + " projects");
						HeaderService.setProjectList(data);

						$rootScope.project = HeaderService.getCurrentProject();
						$rootScope.$broadcast('ProjectListChange');
						$location.path('monitor/' + _currentProject.id);
					
					
					}).catch(function(data) {
						console.log("  Error getting project devices data " + data);
					});
				
				}).catch(function(data) {
					console.log("  Error saving device " + device.name + " (" + device.id + ")");
				});
			}
		}
	}
	
	$scope.navigate = function(path) {
		DeviceService.removeCurrentDevice();
		$location.path(path);
	}
	
	$scope._addParam = function(param) {
		console.log("100");
		console.log("_addParam : " + param.value.key + ", " + param.value.name + ", " + param.value.type + ", " + param.value.showLabel + ", " + param.value.showValue);
		var error = false;
		
		if (param == null || param.value == null || param.value.name == null || param.value.name === "")
			$scope.__param.value.error = "Must include a valid param name";
		
		
		if (_currentDevice == null) {
			console.log("Current device is null. Sets it to empty");
			_currentDevice = {};
			_currentDevice.params = [];
		}
		 
		if (_currentDevice.params == null) {
			console.log("Current device params is null. Sets to empty ");
			_currentDevice.params = [];
		} else {
			for (var i = 0; i < _currentDevice.params.length; i++) {
				if (_currentDevice.params[i].key === param.value.key) {
					$scope.__param.value.error = "Duplicate param key '" + param.value.key + "'. Choose a different key";
					error = true;
					break;
				}
				
				if (_currentDevice.params[i].name === param.value.name) {
					$scope.__param.value.error = "Duplicate param name '" + param.value.name + "'. Choose a different name";
					error = true;
					break;
				}
			}
		}
		
		if (!error) {
			_currentDevice.params.push(param.value);
			this._clearParam();
		}
		
		console.log("_currentDevice: ");
		console.log(_currentDevice);
		$scope.device = _currentDevice;
		/*if ($scope._params == null) {
			$scope._params = [];
		} else {
			for (var i = 0; i < $scope._params.length; i++) {
				if ($scope._params[i].key === param.value.key) {
					$scope.param.value.error = "Duplicate param key '" + param.value.key + "'. Choose a different key";
					error = true;
					break;
				}
				
				if ($scope._params[i].name === param.value.name) {
					$scope.param.value.error = "Duplicate param name '" + param.value.name + "'. Choose a different name";
					error = true;
					break;
				}
			}
		}
		
		if (!error) {
			$scope._params.push(param.value);
			this._clearParam();
		}*/
			
	}
	
	$scope._clearParam = function() {
		$scope.__param.value = {
  		key: null,
			name: null,
			type: "string",
			showLabel: "name",
			showValue: "always",
			error: ""
		}
	}
	
	/** Check if these two functions are needed */
	$scope._deleteParam = function(param) {
		//console.log("     Tries to delete param " + param.name);
		for (var i = 0; i < _currentDevice.params.length; i++) {
			//console.log("      Browsing _params " + $scope._params[i].name);
			if (_currentDevice.params[i].name === param.name) {
				//console.log("      Deletes param " + param.name);
				_currentDevice.params.splice(i, 1);
				break;
			}
		}
	}
	
	/** In monitor.js  Consider removing*/
	$scope.testFunction = function() {
		console.log(" GGGGG TEST FUNCTION");
		return "VALUE"; 
	}
	
	$scope.getParamValue = function(device, key) {
		console.log(" GGGGG GET PARAM VALUE " + devive + "  : " + key);
		var lastRead = device.lastRead;
		
		if (lastRead != null) {
			
			for (var i = 0; i < lastRead.params.length; i++) {
				if (lastRead.params[i].key === key) {
					return lastRead.params[i].value;
				}
			}
			
		} else {
			return null;
		}
	}
	
	$scope._removeParam = function(param) {
		//for (var i = 0; )__params.push(param);
	}
	
	
	// ------------
	
  
	
	prepareCharts = function() {
		console.log("GETS READINGS for " + _currentDevice.id + " :: " + _currentDevice.name);
		DeviceService.getReadings(_currentDevice.id, 1, 40)
	  .then(function(data) { 

			console.log("  Retrieved " + data.length + " readings");
			_currentDevice.readings = data;
			$scope.device = _currentDevice;
			drawChart();
		
		}).catch(function(data) {
			console.log("  Error getting project devices data " + data);
		});
		
		
	}
	
	
	drawChart = function() {
		
		console.log("Draws chart");
		var xdata = [];
		if (_currentDevice.readings != null) {
			console.log("" + _currentDevice.readings.length + " readings");
			xdata.push(["Timeline", "Moisture"]);
			for (var i = 0; i < _currentDevice.readings.length; i++) {
				var reading = _currentDevice.readings[i];
				console.log("  Reading " + reading.received + ": " + reading.data);
				var xreading = [];
				
				var date = new Date(reading.received);
				
				xreading.push(date.toString());
				//xreading.push(i+1);
				
				for (var j = 0; j < reading.params.length; j++) {
					if (reading.params[j].key == "mvalue") {
						xreading.push(parseInt(reading.params[j].value));
					}
				}
				
				//xreading.push(reading.value);
				xdata.push(xreading)
				console.log(xreading);
			}
		}
		
		google.charts.load('current', {'packages':['corechart']});
		google.charts.setOnLoadCallback(function() {
			
      /*var data = google.visualization.arrayToDataTable([
        ['Timeline', 'Moisture', 'Temperature'],
        ['18/10/2016 00:54:29',  3003, 400],
        ['18/10/2016 00:52:35',  3001, 460],
        ['18/10/2016 00:51:32',  3000, 1120],
        ['18/10/2016 00:40:58',  2001, 540]
      ]);*/
			var data = google.visualization.arrayToDataTable(xdata);
				

      var options = {
        title: 'Last 12 hours',
        curveType: 'function',
        legend: { position: 'bottom' },
				chartArea: {left: 100}
      };
			
			//$scope.showChartLoad = false;
      var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));
      chart.draw(data, options);
			
		});
	}
	
	
});
		
		

		

