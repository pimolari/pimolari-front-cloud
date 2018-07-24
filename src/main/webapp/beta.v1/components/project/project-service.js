angular.module('ew.project.service', [])

.value('ProjectHandler', {
		
	check: function(project) {
			
		//console.log("    Checks project content with " + project.devices.length + " devices");
		//console.log("     #" + project.name + " (" + project.id + ") has " + project.devices.length + ' devices');
			
		return project;
	},	
	
	prepareProjects: function(projects, DeviceHandler) {
		for (var i = 0; i < projects.length; i++) {
			projects[i] = this.prepareProject(projects[i], DeviceHandler);
		}
		return projects;
	},
	
	prepareProject: function(project, DeviceHandler) {
		if (project.devices != null) {
			project.devices = DeviceHandler.prepareDevices(project.devices);
		}
		return project;
	}
})
	
	
// Service definition
.service('ProjectService', function($http, $q, ProjectHandler, DeviceHandler) {
		
	// Lists projects
	this.list = function() {
		var defered = $q.defer();
		var promise = defered.promise;
		//console.log('    ProjectService.list() invoked');
			
		$http.get(environment + 'ew/projects/')
		.success(function(data) {
			//console.log("    ProjectService.list() returned " + data.length + " projects");
			defered.resolve(ProjectHandler.prepareProjects(data, DeviceHandler));
		})
		.error(function(err) {
			console.log("ERROR");
			defered.reject(err)
		});
			
		return promise;
	}	
		
	// function get
	this.get = function(id) {
		
		
		var defered = $q.defer();
		var promise = defered.promise;
		console.log('    ProjectService.get('	 + id + ') invoked');
		
		if (id != null || id == "") {	
			
			$http.get(environment + 'ew/projects/' + id + "?filter=devices,lastRead")
			.success(function(data) {
				//console.log("    ProjectService.get() returned project " + (data != null? data.name : "Null object"));
				defered.resolve(ProjectHandler.prepareProject(data, DeviceHandler));
			})
			.error(function(err) {
				//console.log("ProjectService.get() failed " + err);
				defered.reject(err);
			});
			
		} else {
			defered.reject("  Can not retrieve a project with null id");
		}
			
		return promise;
	}
	
	// function monitor
	this.monitor = function(id) {
		
		var defered = $q.defer();
		var promise = defered.promise;
		console.log('    ProjectService.monitor('	 + id + ') invoked');
		
		if (id != null || id == "") {	
			
			$http.get(environment + 'ew/projects/' + id + "/monitor?filter=lastRead")
			.success(function(data) {
				//console.log("    ProjectService.monitor() returned project " + (data != null? data.name : "Null object"));
				//defered.resolve(ProjectHandler.prepareProject(data, DeviceHandler));
				defered.resolve(DeviceHandler.prepareDevices(data, DeviceHandler));
			})
			.error(function(err) {
				//console.log("ProjectService.monitor() failed " + err);
				defered.reject(err);
			});
			
		} else {
			defered.reject("  Can not retrieve a project with null id");
		}
			
		return promise;
	}
		
	// function post saves
	this.post = function(project) {
		var defered = $q.defer();
		var promise = defered.promise;
		
		//console.log('    ProjectService.post(' + project + ') invoked');
		
		if (project != null) {
			
			$http.post(environment + 'ew/projects/', project)
			.success(function(data) {
				//console.log("    ProjectService.post() successfully executed");
				defered.resolve(data);
			})
			.error(function(err) {
				//console.log("    ProjectService.post() failed " + err);
				defered.reject(err);
			});
			
		} else {
			defered.reject("  Can not retrieve a project with null id");
		}
		
		return promise;
		
	}
		
	this.addDevice = function(id, device) {
		var defered = $q.defer();
		var promise = defered.promise;
		//console.log('    ProjectService.addDevice(' + id + ', ' + device + ') invoked');
		return $http.post(environment + 'ew/projects/' + id + '/devices', project);  //TODO: payload implementation 
	}
		
});




		
