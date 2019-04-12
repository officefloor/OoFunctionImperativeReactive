const HttpException = Java.type("net.officefloor.server.http.HttpException");
const Integer = Java.type("java.lang.Integer")
const RequestIdentifier = Java.type("net.officefloor.demo.RequestIdentifier")

function validate(identifier, requestIdentifier) {
	if (Number(identifier) <= 0) {
		throw new HttpException(422, "Invalid identifier");
	}
	requestIdentifier.set(Integer.valueOf(identifier))
}
validate.officefloor = [ 
	{ httpPathParameter: "identifier" },
	{ out: Integer, qualifier: RequestIdentifier },
	{ next : "valid" }
];