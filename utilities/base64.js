
const idPA = process.env.ID_PA || "77777777777"
const idBrokerPA = process.env.ID_BROKER_PA || "77777777777"
const idStation = process.env.ID_STATION || "77777777777_01"

xmlDatiSpecificiServizioBolloAuto = `
<?xml version="1.0" encoding="UTF-8"?>
<ta:tassaAuto xmlns:ta="http://PuntoAccessoPSP.spcoop.gov.it/TassaAuto"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://PuntoAccessoPSP.spcoop.gov.it/TassaAuto TassaAutomobilistica_1_0_0.xsd ">
    <ta:veicoloConTarga>
        <ta:tipoVeicoloTarga>1</ta:tipoVeicoloTarga>
        <ta:veicoloTarga>AB345CD</ta:veicoloTarga>
    </ta:veicoloConTarga>
</ta:tassaAuto>
`

xmlDatiSpecificiServizioDonazioni = `
<?xml version="1.0" encoding="UTF-8"?>
<dona:donazione xmlns:dona="http://PuntoAccessoPSP.spcoop.gov.it/Donazioni"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://PuntoAccessoPSP.spcoop.gov.it/Donazioni Donazioni_1_0_0.xsd ">
    <dona:donation>
        <dona:creditorInstitution></dona:creditorInstitution>
        <dona:amount>1000</dona:amount>
    </dona:donation>
</ta:donazione>
`

idService=1
// select service
switch(idService) {
  case 1:
    xml=xmlDatiSpecificiServizioDonazioni
    break;
  case 2:
    xml=xmlDatiSpecificiServizioBolloAuto
    break;
  default:
    xml=xmlDatiSpecificiServizioDonazioni
}

xmlDatiSpecificiServizio=Buffer.from(xml).toString('base64')

// wsdl definition
// https://github.com/pagopa/pagopa-api/tree/SANP3.0.0
paDemandPaymentNoticeRequest=`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:pafn="http://pagopa-api.pagopa.gov.it/ec/PaForNode.xsd">
  <soapenv:Header />
  <soapenv:Body>
    <pafn:paDemandPaymentNoticeRequest>
      <idPA>${idPA}</idPA>
      <idBrokerPA>${idBrokerPA}</idBrokerPA>
      <idStation>${idStation}</idStation>
      <idServizio>${idService}</idServizio>
      <datiSpecificiServizio>${xmlDatiSpecificiServizio}</datiSpecificiServizio>
    </pafn:paDemandPaymentNoticeRequest>
  </soapenv:Body>
</soapenv:Envelope>
`

paDemandPaymentNoticeRequestName=`paDemandPaymentNoticeRequest`
let nomeFile=`${idService}-${paDemandPaymentNoticeRequestName}.xml`
console.log(nomeFile)
require("fs").writeFileSync(nomeFile, paDemandPaymentNoticeRequest);

