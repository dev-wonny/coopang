# 📦 Coopang 📦
![Logo](./image/coopangLogo.png)

## 🚚 COOPANG 서비스 소개
* 본 시스템은 MSA(Microservices Architecture)와 스프링 부트(Spring Boot)를 기반으로 하며, 쿠팡의 풀필먼트 서비스에서 영감을 받아 설계되었습니다.
* 상품의 입고부터 출고, 배송에 이르는 전체 물류 프로세스를 효율적으로 관리할 수 있도록 구성되었습니다.
* 시스템은 허브스포크(Hub-and-Spoke) 방식으로 설계되어, 물품이 물류 허브에서 고객 근처의 허브로 이동한 후 최종적으로 고객에게 전달됩니다.



## ✅ How to start
### To start the Docker services you defined in your docker-compose.yml file, follow these steps:

1. Ensure Docker and Docker Compose are installed: Make sure Docker is running on your machine and Docker Compose is installed.

2. Navigate to the directory where your docker-compose.yml file is located.

3. Start the services: Run the following command in the terminal:

```bash
docker-compose up -d
```

4. Verify the services are running: To check if the containers are up and running, you can use:

```bash
docker ps
```
This will show the list of running containers, including postgres, redis-stack, prometheus, grafana, and loki.


If you want to stop the services later, run:

```bash
docker-compose down
```
This will stop and remove all the containers created by this compose file.



```bash
```
