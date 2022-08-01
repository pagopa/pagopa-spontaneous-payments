import http from 'k6/http';

const ciCode = "12345678";

export function getCiCode(id) {
	return ciCode + ('000'+id).slice(-3);
}