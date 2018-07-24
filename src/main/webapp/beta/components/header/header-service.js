	angular.module('ew.header.service', [])

	
	// Service definition
	.service('HeaderService', function($http) {
		
		var _projects = [];
		var _currentProject = null;
		var _currentDevice = null;
		
		
		
		/***/
		this.setProjects = function(projects) {
			console.log("    Header Service sets projects " + projects + " Total: " + projects.length + " items");
			if (projects != null && projects.length > 0) {
				_projects = projects;
				//$rootScope.$broadcast('projectList_change');
				if (_currentProject === null) {
					this.setCurrentProject(this.getDefaultProject());
				}
			}
			return _projects;
		}	
	
		/***/
		this.getProjects = function() {
			if (_projects != null) {
				console.log("    Header Service returns " + _projects.length + " projects");
				return _projects;
			} else {
				console.log("    Header Service returns null project list");
			}
			return null;
		}

		/***/
		this.getProject = function(projectId) {
			console.log("    Headers Service retrieves project with id " + projectId);

			for (var i = 0; i < _projects.length; i++) {
				if (new String(_projects[i].id).valueOf() == new String(projectId).valueOf()) {
					return _projects[i];
				}
			}
			return null;
		}
		
		/***/
		this.addProject = function(project, isDefault) {
			console.log("    Header Services adds project to list " + project);
			_projects.push(project);
			
			if (isDefault) {
				this.setCurrentProject(project);
			}
		}

		/***/
		this.setCurrentProject = function(project) {
			if (project != null) {
				console.log("    Header Services sets current project to " + project.name);
				_currentProject = project;
				//$rootScope.$broadcast('currentProject_change');
			}
			return _currentProject;
		}
		
		this.setCurrentProjectById = function(projectId) {
			console.log("    Header Services sets current project with id " + projectId);
			
			if (projectId != null && projectId != "") {
				var project = this.getProject(projectId);
				return this.setCurrentProject(project);			
			}
		}
		
		this.getCurrentProject = function() {
			console.log("    Headers Service retrieves current project " + _currentProject);
			return _currentProject;
		}
		
		/***/
		this.getDefaultProject = function() {
			
			if (_projects != null && _projects.length > 0) {
				for (var i = 0; i < _projects.length; i++) {
					if (_projects[i].default == true) {
						console.log("    Headers Service chooses default project " + _projects[i].id);
						return _projects[i];
						break;
					}
				}
				console.log("    Headers Service chooses default project automatically " + _projects[0].id);
				return _projects[0];
			}
			
			return null;
		}
		
		this.addDevice = function(projectId, device) {
			if (projectId != null) {
				
				var referencedProject = this.getProject(projectId);
				if (referencedProject.devices != null) {
					console.log("  Device is added to project **** ");
					referencedProject.devices.push(device);
					return referencedProject;
				}
				/*for (var i = 0; i < _projects.length; i++) {
					if (new String(_projects[i].id).valueOf() == new String(projectId).valueOf()) {
						if (_projects[i].devices != null) {
							_projects[i].devices.push(device);
							return _projects[i];
						}
					}
				}*/
			}
			
			return null;
		}
		
		this.updateDevice = function(projectId, device) {
			if (projectId != null) {
				var referencedProject = this.getProject(projectId);
				for (var i = 0; i < referencedProject.devices.length; i++) {
					if (new String(referencedProject.devices[i]).valueOf == new String(device.id)) {
						console.log("  Device is updated in project **** ");
						referencedProject.devices[i] = device;
						return true;
					}
				}
			}
			return false;
		}
		
		this.getDeviceInCurrentProject = function(id) {
			
			if (_currentProject != null) {
				if(_currentProject.devices != null) {
					for (var i = 0; i < _currentProject.devices.length; i++) {

						if (new String(_currentProject.devices[i]).valueOf == new String(id)) {
							return _currentProject.devices[i];
						}
					}
				}
			} 
			
			return null;
		}
		
		this.getDevice = function(projectId, id) {
			
			var referencedProject = this.getProject(projectId);
			
			if (referencedProject != null) {
				if(referencedProject.devices != null) {
					for (var i = 0; i < referencedProject.devices.length; i++) {
						if (new String(referencedProject.devices[i]).valueOf == new String(id)) {
							return referencedProject.devices[i];
						}
					}
				}
				
			} else {
				return null;
			}
		}
		
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
		
		
		
	});




		
