angular.module('ew.monitor.service', [])
.value('MonitorHandler', {

	check: function(DeviceHandler, devices) {
		console.log("Checks devices list " + devices.length);

		for (var i in devices) {
			var device = devices[i];
			console.log(" #" + i + " " + device.name + " (" + device.id + ") :: " + device.data);
		}
		return devices;
	},
		
	prepareReadings: function(DeviceHandler, devices) {
		
		console.log("  Prepares readings view for each device in project ");
			
		//hvalue=604,phvalue=7
		if (devices != null) {
			for (var i = 0; i < devices.length; i++) {
				console.log("  Device: " + devices[i].name + " Last reading: " + devices[i].lastRead);
				if (devices[i].lastRead != null) {
					devices[i].lastRead.params = DeviceHandler.parseReading(devices[i].lastRead.data);
					//console.log("successfully parsed params: " + devices[i].params.name + );
				}
			}
		}
			
		return devices;
	}
})
	
// Service definition
.service('MonitorService', function($http) {
		
	
		

		
		
});




		
