Test service
pre-condition: service nel DB
1. get services
2. get service

Test org
pre-condition: service nel DB
1. create org senza enrollments
2. post /org/serv per creare un enrollment
3. update del servizio
4. update del org
5. get org
6. get serv
7. delete servi
8. delete org

Test POST spontaneous payment
pre-condition: org e servizio esistenti ed associati
1. invocare la post


test flusso aggiunta servizio
1. creazione org con 1 enrollment
2. org aggiunge servizio

test flusso cancellazione servizio
1. creazione org con 2 enrollment
2. cancella enrollment

test flusso 
