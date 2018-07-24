	angular.module('ew.device.service', [])

  .value('DeviceHandler', {
	  
		check: function() {
			console.log("  DeviceHandler.check");
		},
		
		prepareDevices: function(devices) {
			console.log("    Device handler prepares devices readings... ");
			if (devices != null) {
				for (var i = 0; i < devices.length; i++) {
					console.log("      > " + devices[i].id);
					devices[i] = this.prepareDevice(devices[i]);
				}
				return devices;
			}
			return null;
		},
		
		prepareDevice: function(d) {
			
			if (d != null) {
				
				/** Display object filled with params */
				
				if (d.lastRead != null) {
					d.lastRead.params = this.parseReading(d.lastRead);
				} else {
					d.lastRead = {};
				}
			
				if (d.readings != null) {
					d.readings = this.prepareReadings(d.readings);
				}
				
				d.lastRead.display = [];
				
				/** Diplay array */
				if (d.params != null) {
					
					for (var i = 0; i < d.params.length; i++) {
						var param = d.params[i];
						var display = null;
						//display = {};
						console.log("    Device handler says Device " + d.name + " with param " + param.name);
						
						display = this.prepareDisplay(param, d.lastRead);
						
						if (display != null)
							d.lastRead.display.push(display);	
					}
				}
				
				/*if (d.lastRead.params != null && d.lastRead.params.length > 0) {
					d.display = [];
					for (var i = 0; i < d.lastRead.params.length; i++) {
						if (d.lastRead.params[i].key === d.key) {
							d.display.push(d.lastRead.params[i].value);
						}
					}
				}*/
				
				return d;
			}
			return null;
		},
		
		prepareDisplay: function(param, lastRead) {
			
			var display = null;
			
			console.log("     Param key: " + param.key);
			console.log("     Param name: " + param.name);
			console.log("     Param showLabel: " + param.showLabel);
			console.log("     Param showValue: " + param.showValue);
			
			if (param.showLabel != "none") {
				display = {};
				/*switch(param.showLabel) {
					case "name":
						display.label = param.name;
					break;
					case "name-key":
						display.label = param.name + " (" + param.key+ ")";
					break;
					case "key":
						display.label = param.key;
					break;
					case "key-name":
						display.label = param.key + " (" + param.name+ ")";
					break;
				  default:
						display.label = param.name;
					break;
				}*/
				
				if (param.showLabel === "name") {
					display.label = param.name;
				} else if (param.showLabel === "name-key") {
					display.label = param.name + " (" + param.key+ ")";
				} else if (param.showLabel === "key") {
					display.label = param.key;
				} else if (param.showLabel === "key-name") {
					display.label = param.key + " (" + param.name+ ")";
				}
			}
						
			if (param.showValue != "never") {
				var _value = null;
				if (lastRead != null && lastRead.params != null) {
					for (var i = 0; i < lastRead.params.length; i++) {
						if (lastRead.params[i].key == param.key) {
							_value = lastRead.params[i].value;
							break;
						}
					}
				}
				
				console.log("   Param value: " + _value);
				
				/*switch(param.showValue) {
				  case "always":
						display.value = _value;
					break;
					case "as-received":
						if (_value != null && _value != "") {
							display.value = _value;
						}
					break;
			  	default:
						display.value = _value;
				  break;
				}	*/

				if (param.showValue === "always") {
					if (display === null) {
						display = {};
					}
						
					display.value = _value;
				} else if (param.showValue === "as-received") {
					if (display === null) {
						display = {};
					}
					if (_value != null && _value != "") {
						display.value = _value;
					}
				}
				
			}
			
			return display;
			
		},
		
		prepareReadings: function(readings) {
			
			if (readings != null) {
				for (var i = 0; i < readings.length; i++) {
					readings[i].params = this.parseReading(readings[i]);
				}
			
				return readings;
			}
			
			return null;
		},
		
		parseReading: function(reading) {
		
			var params = [];
			
			if (reading != null && reading.data != null) {
				//hvalue=604,phvalue=7
				var iParams = reading.data.split(",");
				
				for (var i = 0; i < iParams.length; i++) {
					var iParam = iParams[i].split("=");
					params.push({key: iParam[0], value: iParam[1]});
				}
			}
			
			return params;
		}	
	})
	
	
	// Service definition
	.service('DeviceService', function($http, $q, DeviceHandler) {
		
		var _currentDevice = null;
		
		this.setCurrentDevice = function(device) {
			if (device != null)
				_currentDevice = device;
			
			return _currentDevice;
		}
		
		this.getCurrentDevice = function() {
			return _currentDevice;
		}
		
		this.removeCurrentDevice = function() {
			_currentDevice = null;
			return;
		}
		
		// Lists devices in project
		this.listByProject = function(projectId) {
			
			var defered = $q.defer();
			var promise = defered.promise;
			console.log('    Device service lists devices in project with id '	 + id);
			
			$http.get(environment + 'ew/projects/' + projectId + "/devices")
			.success(function(data) {
				//console.log("    DeviceService.listByProject() returned " + data.length + " devices");
				defered.resolve(DeviceHandler.prepareDevices(data));
			})
			.error(function(err) {
				defered.reject(err)
			});
			
			return promise;
		}	
		
		// function get
		this.get = function(id) {
			var defered = $q.defer();
			var promise = defered.promise;
			//console.log('DeviceService.get(' + id + ') invoked');

			$http.get(environment + 'ew/devices/' + id)
			.success(function(data) {
				//console.log("    DeviceService.get(" + id + ") returned " + data != null? data.name : "null");
				defered.resolve(DeviceHandler.prepareDevice(data));
			})
			.error(function(err) {
				defered.reject(err)
			});
			
			return promise;
		}
		
		/** 
		* Device save function
		* Accepts a Device object parameter and requests a post operation on device
		* This method should not set the lastRead parameter. 
		* TODO: In the future, setting lastRead should not be allowed from the server side
		*/

		this.post = function(d) {
			var device = {};
			device.name = d.name;
			device.description = d.description;
			//device.lastRead = d.lastRead;
			device.params = d.params;
			device.projectId = d.projectId;
			device.type = d.type;
			
			var defered = $q.defer();
			var promise = defered.promise;
			//console.log('DeviceService.post(' + device + ') invoked');
			
			$http.post(environment + 'ew/devices/', device)
			.success(function(data) {
				//console.log("    DeviceService.post() returned " + data.name);
				defered.resolve(data);
			})
			.error(function(err) {
				defered.reject(err)
			});
			return promise;
		}
		
		/** 
		* Device update function
		* Accepts a Device object parameter and requests a put operation on device
		* This method should not set the lastRead parameter. 
		* TODO: In the future, setting lastRead should not be allowed from the server side
		*/
		this.put = function(d) {
			var device = {};
			//console.log(" PUTS DEVICE: " + d.id + ": " + d.name);
			device.id = d.id;
			device.name = d.name;
			device.description = d.description;
			//device.lastRead = d.lastRead;
			device.params = d.params;
			device.projectId = d.projectId;
			device.type = d.type;
			
			var defered = $q.defer();
			var promise = defered.promise;
			console.log('DeviceService.put(' + device + ') invoked');  
			
			$http.put(environment + 'ew/devices/', device)
			.success(function(data) {
				//console.log("    DeviceService.put() returned " + data.name);
				defered.resolve(data);
			})
			.error(function(err) {
				defered.reject(err)
			});
			return promise;
		}
		
		/** 
		* Device enable function
		* Accepts a Device object, forces active=true and calls this.update
		*/
		this.enable = function(d) {
			d.active = true;
			return this.update(d);
		}
		/** 
		* Device disable function
		* Accepts a Device object, forces active=true and calls this.update
		*/
		this.disable = function(d) {
			d.active = false;
			return this.update(d);
		}
		
		this.getReadings = function(id, page, max) {
			var defered = $q.defer();
			var promise = defered.promise;
			//console.log('DeviceService.getReadings(' + id + ') invoked');
			
			$http.get(environment + 'ew/devices/' + id + '/readings?page=' + page + '&max=' + max)
			.success(function(data) {
				//console.log("    DeviceService.getReadings() returned " + data.length + " readings");
				defered.resolve(DeviceHandler.prepareReadings(data));
			})
			.error(function(err) {
				defered.reject(err)
			});
			return promise;
		}
		
	});




		
