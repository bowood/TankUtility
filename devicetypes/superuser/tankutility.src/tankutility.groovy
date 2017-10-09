preferences {
	input("token", "string", title:"Authentication Token", description: "Authentication Token", required: true, displayDuringSetup: true)
	input("deviceID", "string", title:"Device ID", description: "Device ID", required: true, displayDuringSetup: true)
}

metadata {
	// Automatically generated. Make future change here.
	definition (name: "TANKUTILITY", author: "bowood") {
		capability "Energy Meter"
		capability "Polling"
		capability "Refresh"
		capability "Sensor"
		capability "Power Meter"
        capability "Temperature Measurement"
	}

	// UI tile definitions
	tiles (scale: 2){
		
		valueTile(	"level", "device.level", width: 4, height:4) 		{
			state(	"device.level",label:'${currentValue} %', backgroundColors:[
			[value: 30, color: "#bc2323"],
			[value: 40, color: "#d04e00"],
			[value: 50, color: "#f1d801"],
			[value: 60, color: "#44b621"],
			[value: 70, color: "#90d2a7"],
			[value: 80, color: "#1e9cbb"],
			[value: 90, color: "#153591"]
			]
			)
		}
		
		valueTile(	"temperature", "device.temperature",width: 2,  height: 1 ) {
			state("device.temperature", label:'${currentValue} F' )
		}
		        
		valueTile("refresh", "command.refresh",width: 2,  height: 2) {
			state "default", label:'refresh', action:"refresh.refresh", icon:"st.secondary.refresh-icon"
		}
		
		main(["level"])
        
		details(["level","refresh","temperature"])
	}
}

def poll() {
	log.trace 'Poll Called'
	runCmd()
}

def refresh() {
	log.trace 'Refresh Called'
	runCmd()
}

def runCmd() {

	def params = [
    	uri: "https://data.tankutility.com/api/devices/$deviceID?token=$token"
	]

    try {
        httpGet(params) { resp ->   
            log.debug "response data: ${resp.data}"
            log.debug "temperature: ${resp.data.device.lastReading.temperature}"
            log.debug "level: ${resp.data.device.lastReading.tank}"
			sendEvent (name: "temperature", value: (resp.data.device.lastReading.temperature).toInteger(), unit:"F")
			sendEvent (name: "level", value: (resp.data.device.lastReading.tank).toFloat(), unit:"%")
        }
    } catch (e) {
        log.error "something went wrong: $e"
    }

}

def parse(String description) {
	log.debug "Got parse: $description"
	return
}