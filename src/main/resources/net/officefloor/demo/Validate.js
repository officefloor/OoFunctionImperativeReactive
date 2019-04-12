const HttpException = Java.type("net.officefloor.server.http.HttpException");
const Integer = Java.type("java.lang.Integer")

function validate(identifier, requestIdentifier) {
	if (Number(identifier) <= 0) {
		throw new HttpException(422, "Invalid identifier");
	}
	requestIdentifier.set(Integer.valueOf(identifier))
}
validate.officefloor = [ 
	{ httpPathParameter: "identifier" },
	{ out: Integer },
	{ next : "valid" }
];