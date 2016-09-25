# Play application

#### GET
```sh
curl -X GET -H "Content-Type: application/json" "http://localhost:9000/users"
```

#### GET/:id
```sh
curl -X GET -H "Content-Type: application/json" "http://localhost:9000/users/0"
```

#### POST
```sh
curl -X POST -H "Content-Type: application/json" -d '{"firstName":"Susa","lastName":"Carmona V", "email":"susa@gmail.com"}' "http://localhost:9000/users"
```

#### PUT/:id
```sh
curl -X PUT -H "Content-Type: application/json" -d '{"id":2, "firstName":"Susana","lastName":"Carmona Valderrama", "email":"susanita@gmail.com"}' "http://localhost:9000/users/2"
```

#### DELETE/:id
```sh
curl -X DELETE -H "Content-Type: application/json" "http://localhost:9000/users/0"
```