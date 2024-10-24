package com.coopang.product.infrastructure.configuration;

import com.coopang.product.application.request.product.ProductDto;
import com.coopang.product.domain.entity.category.CategoryEntity;
import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.entity.productstock.ProductStock;
import com.coopang.product.domain.entity.productstock.ProductStockEntity;
import com.coopang.product.domain.entity.productstockhistory.ProductStockHistoryChangeType;
import com.coopang.product.domain.entity.productstockhistory.ProductStockHistoryEntity;
import com.coopang.product.infrastructure.repository.category.CategoryJpaRepository;
import com.coopang.product.infrastructure.repository.product.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "data.init.enabled", havingValue = "true", matchIfMissing = false)
class ProductDataInitializer implements ApplicationRunner {

    private final CategoryJpaRepository categoryRepository;
    private final ProductJpaRepository productJpaRepository;
    private int uuidIndex = 0;
    private int uuidIndex2 = 0;
    private Random random = new Random();

    private String[] categoryValues = new String[] {
        "식품", "음료", "식자재", "가공식품", "의류", "패션잡화", "신발", "생활용품", "가전제품", "디지털 기기",
        "화장품", "유아용품", "건강", "스포츠", "가구", "자동차용품", "펫용품", "취미", "공구", "도서"
    };

    private String[] productNames = new String[] {
        "유기농 바나나", "콜드브루 커피", "천연 올리브유", "즉석밥", "남성용 정장",
        "여성용 핸드백", "스니커즈 운동화", "휴대용 가습기", "무선 이어폰", "스마트폰",
        "스킨케어 토너", "유아용 기저귀", "비타민C 보충제", "요가 매트", "원목 침대",
        "차량용 블랙박스", "강아지 사료", "DIY 공예 세트", "드릴 세트", "베스트셀러 소설",
        "무농약 사과", "아메리카노 원두", "콩기름", "라면", "여성용 원피스",
        "남성용 구두", "캐주얼 백팩", "세탁기", "스마트 워치", "게이밍 키보드",
        "립스틱", "유아용 장난감", "오메가3 캡슐", "런닝화", "책상",
        "차량용 공기청정기", "고양이 모래", "취미용 드론", "망치", "자기계발서",
        "친환경 토마토", "핫초코", "참기름", "캔 통조림", "남성용 자켓",
        "지갑", "구두", "에어프라이어", "게임 콘솔", "태블릿 PC",
        "미백 크림", "유아용 카시트", "프로틴 파우더", "헬스장 장갑", "소파",
        "차량용 내비게이션", "고양이 캔", "DIY 페인팅 키트", "전동 드라이버", "요리책",
        "친환경 감자", "아이스티", "참치캔", "초콜릿", "여성용 코트",
        "모자", "아동용 운동화", "청소기", "액션 카메라", "게이밍 마우스",
        "메이크업 브러쉬", "아기침대", "칼슘 보충제", "축구공", "의자",
        "차량용 충전기", "고양이 간식", "모형 자동차", "톱", "여행 가이드북",
        "무농약 블루베리", "레몬에이드", "햄", "비스킷", "남성용 셔츠",
        "가죽 벨트", "부츠", "믹서기", "스피커", "전자책 리더기",
        "페이스 마스크", "유모차", "근육 마사지건", "야외 텐트", "서랍장",
        "차량용 방향제", "강아지 옷", "DIY 수제 비누 키트", "수공구 세트", "어린이 그림책"
    };

    String[] fixedUuids = {
        "d48209d3-d50e-43d5-b053-6182a8707b59", "327f4b48-e276-4f57-8a47-3c7e7dec657f", "aa2d9e81-0055-4518-b1d2-045adc4ac73b",
        "e6e2784f-ff9e-44f5-98a7-1ae49cec997f", "1e9dd9a8-a708-4d8c-955a-6c9753aae6da", "f5a5cfd8-899b-49a8-8702-7c2008f8c4ec", "434041b3-bc40-4ec7-a1d8-f4fe3992819e",
        "aae99f4f-f1ee-4a10-80da-b21b9e6aa191", "e140faa0-4e99-4438-8576-9d0c27052bd3", "b4beb137-a459-4979-87d9-d180322f50d1", "51bf0b38-83e9-4ab6-af73-ba77acf27525",
        "b8064569-4bf5-4778-97ed-d7757dc8ffb2", "699dbac0-7d98-4c29-8a7f-1dd0ef4099de", "df1653c2-125a-41f8-a72b-66bb747bc512", "38f97c32-a1f9-4357-9bcb-f2760d4e6c8a",
        "50bd1324-7f54-4909-8852-d9df39189b5b", "a04697ce-e0ce-4d24-ba84-1a4b0ab21882",
        "4efb8dc8-e4c3-4519-8e46-57a3b297c434", "6f2ad1f8-9b5c-4110-8f05-dd733afce69c", "b84d6885-6153-4953-82db-4be82640fbc0", "14eb8025-78ef-4f39-92d0-140ebd903f98",
        "9607f7b4-46fc-406a-a325-63eda0aca71c", "f05f33f2-1bca-4805-a9f2-12055167c86c", "64004ac1-765a-4d3c-8b39-6a769d0ad8bd", "a72df252-054f-4b92-b691-3e0a6405bb56",
        "cc0197ca-e936-42f6-8e88-48a3ec2a1d5c", "2011a66c-3afd-47be-b23d-9660e8bfa2c8", "0f5f5104-6bca-44f3-8fd2-103702467e8c", "dcd39610-0adf-4581-bc18-bf6c84cde793",
        "b146ebba-0313-4bae-bc8d-bab3cbec3099",
        "6e53b9a6-6ea6-437f-8f4d-52e376ab9310",
        "600a83cf-933a-480b-8c45-897b923e2458",
        "98455832-9e2a-42fe-b9d6-400cd7e78a8e",
        "da5d82a2-e0db-413a-b608-f20327666574",
        "eb142a69-f8f6-4d55-a64b-e0c4b68c1b57",
        "cd1150e1-9da1-48b4-a670-46849d170096",
        "60201cb1-a739-4a4e-9a31-a00baf333f9e",
        "0c8fbc18-1969-42b7-87e6-196362b73d44",
        "f11f3cb2-9053-4f3b-ad8b-f6bb504d6542",
        "01f787d1-5810-4d15-8a37-ef793ae17457",
        "24d2bffc-7702-432d-acff-1f260c9b289d",
        "035160ed-4b52-4ea5-bb22-ac50c4fb5bdb",
        "7d884040-312e-4a17-8610-2e9ad389a455",
        "739ef34f-43aa-4a7a-9f76-ef006a7f8146",
        "be3bbe7e-2e0a-4bd4-afcd-a7cabcd4385b",
        "d440b07e-9033-4175-84fa-c1010dd2778a",
        "6efea0bb-1031-4f77-864a-1067735fc2cf",
        "07ad655d-7f54-4d7c-8171-0b5427d650f1",
        "cac115ef-6cf0-4f8c-b11b-3bd474bbdcc5",
        "4ca7f447-c90d-4bf2-8681-8686c0ca6531",
        "4497a40e-dcf0-4a14-ac2c-4a403639b8fc",
        "936ee2b5-666e-4ea8-afdb-e0bd82b31fc3",
        "daa9f8ad-26b1-4acf-acd2-b4bdca5f22fa",
        "1eb4a5a3-1ce7-4fb9-918f-a7cafa3d9ee4",
        "c53a8835-56a8-4f3f-afee-cd70f9130734",
        "5bd667d3-ca27-4415-a297-2ccddce5d325",
        "49ec250f-8d06-4ab4-964c-5293b16375ac",
        "3ef6e443-26f8-45df-b433-a72f51b54cc4",
        "6bb39ce1-17e4-4c35-b587-3d30d605b3c4",
        "907db52c-75cd-4fcc-8437-75d770adcbee",
        "d9a597ae-eeaf-4765-9dda-e0ba54addad4",
        "a31edab1-9b67-4d27-a6b4-69e142d98f23",
        "00c2bae4-d491-4ba2-8383-a1653d34fa79",
        "28554967-6bf4-44cb-9d44-03116a027005",
        "3319444e-b272-44a2-adac-0c6d03e206f5",
        "6dfd9fae-b66e-4b17-84c4-74dd490bd3d3",
        "e1be21e3-38ef-48e0-b6b6-328e071f87fb",
        "4a4ccd0d-e49d-47df-9d05-68bd1fe7d913",
        "8d87cbe9-8aac-46d6-b3ae-b7f8fffb5c6d",
        "13d3bf06-19ab-497c-8cb0-9dccb9db98b7",
        "a4f81587-e0a7-483c-9009-7f78d05255bd",
        "78fe6c22-0573-43f5-a88e-928f723597df",
        "18ed4312-2647-4cee-b988-818145fe634a",
        "116554d9-c98d-405d-845d-324a6854e21d",
        "6d7ab2f1-0a66-425e-8930-48daa206e073",
        "5b2ac0c6-4779-4adf-8af0-0694e9bc670f",
        "3a5da999-79e5-49d8-b280-8f4250613232",
        "1f5e7ac4-80c9-479a-a638-995226f3074a",
        "dbc157b7-bb6c-454c-9dae-3eb030b30cf5",
        "3304fe6c-d678-49e0-bf41-f9751150c616",
        "329fbfd1-ab1d-491a-9ce0-5b34b1c42103",
        "aaf4c41b-6f83-40b2-b46d-23facd7a3946",
        "18701717-ef56-49e2-8d03-d1799b15faad",
        "9cdf3a75-2c2a-4a1d-8e69-f1792d2a53bc",
        "a069f8a8-de64-4a12-acb2-4a5449ce1dca",
        "8b032ca0-1586-4a93-9fd1-061ff406bf22",
        "7c632d37-68d9-4c1d-9e6e-060a55d5bdc7",
        "600d856c-f0cf-4985-aa91-998388a31f68",
        "82327fe0-3ab3-4672-a526-5fb5f09ea4a5",
        "84a626e8-f455-48c8-ac54-9c70917e5c48",
        "0d43e6c4-8c8e-498e-bf62-c55232bbdbe1",
        "d9a59fcb-9b1b-4fae-8c0e-1f6edefa6a1f",
        "869ccab4-accc-4f9e-a823-170e8ebb62b7",
        "f6814e91-a900-4a16-a8b0-1a855e8746cc",
        "c75afabb-ea0c-423c-9bf5-3feb7f0ad8f5",
        "6f3088ff-3bed-4690-82b2-33ff71083b70",
        "6606809d-f051-459a-be2e-fbaf0500ce5a",
        "2f3a83e1-48a1-4415-8122-c0ac6948fe7d",
        "1c3eb4b6-5783-4710-ba10-5b1532b51c82",
        "968a3ab8-fdca-4144-b1cd-d1eff6c27623",
        "22bae81c-0992-482f-9a9b-367a3aa8d48f",
        "a2baf6e9-b455-41bc-9c63-39c6a91cd2e8",
        "7e8d1c13-0ede-4f04-9263-626ecd4c5dd5",
        "d0f039b6-1827-4943-954f-71f1e0b2f797",
        "54017f1d-9621-4105-8672-a2c2617e0bcc",
        "b5f73fb7-07c7-4dc0-9f90-81920f7da787",
        "3da4a6ee-f6b5-46cc-9b78-45306491bf05",
        "aa7db283-ea84-4078-b527-7a98b369fccc",
        "f997f883-4019-45c0-a342-d8c0b58415c8",
        "32d3eebc-1775-4807-b47c-6a126885846c",
        "d8818f85-24c9-4a72-b1b2-cb4c1a3fe526",
        "71c49cc2-9638-4904-9431-95cda2d1e087",
        "9c1884d0-7ea9-4f11-be56-6b697f42c67c",
        "d0e3426b-4df2-42df-9220-ed43ca333f33",
        "e4349b2d-4945-4908-b87a-79c2be5cb07c",
        "6b93089c-476a-4aa7-97c7-2558dfc1216c",
        "7d490006-a7de-4798-92db-a9e8ddb2b2de",
        "6e5a1f12-314c-450a-873a-e306a15296b2",
        "9a2b61c5-5376-4b0a-b075-7bf647aedbb8",
        "1b6e1ae3-86d2-4394-9c74-7f5d4de221aa",
        "a62dfe17-1c09-4be0-95a4-1001f1f3960d",
        "bf86b008-e65e-48cb-8c17-35887e7527b6",
        "59176429-655e-4564-881a-133417cb3aae",
        "8c1b2d89-0b9a-4e5b-979b-babf5863449d",
        "f2f80c8c-aa30-44b4-a5bc-0088ef833c71",
        "de06957f-f393-44d1-9e62-46dd34ce061d",
        "5bc0a900-eeaf-46d0-8cf8-e1795cb8d7bd",
        "ac109279-55d1-47d6-be6a-baf9f4d67002",
        "3776d99e-dde8-4675-a654-aceebd212246",
        "496dd6c8-9d48-4b12-b22b-0c7dd06014c2",
        "6e6a0d7e-2347-4a66-bb6c-dc626c71ddbe",
        "3b68cea2-a789-4df6-bb40-30623e26ebfe",
        "28a443a0-a1e7-4fb7-b2d1-0ff410dcd3e3",
        "edfa377d-a1a8-4c99-8324-dd4e0bd05e75",
        "2320be35-d037-4d13-ba06-9275a636ad21",
        "d268be32-00a7-4a72-b80f-7e60463e2c2b",
        "660c9438-9b3d-4ca4-9f72-6d3d7c8d49d7",
        "c5019b03-c7d1-47ed-b5c4-e3c7d75cf24b",
        "93c62f6d-2dbe-48d4-8bae-d83b03e614c4",
        "e044daac-2761-4609-8267-99be40780c18",
        "1fbef56a-b407-4f1e-80d2-0b68d842582a",
        "fc212ee4-1f17-411f-a779-a49c1a66c437",
        "6c72922e-951a-442b-b129-b2f9695c2942",
        "74f3a438-36b1-40d8-b068-26edcb089c8d",
        "13811ff9-f661-4661-818d-d77d112633af",
        "4c7c01a4-5bc1-4067-a97e-70309b60dd41",
        "2b93e007-81e7-4564-a87a-33c0ab3990c6",
        "2a97397d-0fa5-49fd-90e6-2eafd4203ed9",
        "7e6e2c44-67cd-45a0-b249-9b5ea0c9b991",
        "6337f9b2-8405-492c-8925-88d01099a16b",
        "38a26412-49a9-4d58-82aa-2a123b07931c",
        "5ce9bf37-529c-4ade-83c3-6d2b206808f9",
        "0eac111b-5615-4f4a-914f-bd3cfa66132a"};

    String[] fixedArrCompanyId = {
        "c1f3f377-ae3c-4a87-bd32-82af9fbb580b"
        , "a0c97890-ab92-4171-a14b-c400a14154d8"
        , "ada2e5b4-5645-4af5-aef4-78cbf0534ecf"
        , "517d910f-ef13-455d-b5b0-9ab10be60773"
        , "7fc7fa2a-77ec-49d6-8a66-2e7b43644672"
        , "fa0b55c9-41c6-41c7-aebc-3a75c7a34d00"
        , "232c60e4-aa01-4ee2-ac9d-538e30e9f7ff"
        , "7268f90d-deb1-496b-9f02-45616bc285d9"
        , "777c0206-e84c-4dd9-ad14-e592932f6649"
        , "786217e4-20bb-49ac-a128-c4f180a2c2af"
        , "69bdc526-ec5c-452a-95a9-b0d98ea08938"
        , "a7c06d05-e0da-4750-bc72-ba094377fa8e"
        , "e3e2f7c2-8b5f-4e47-aefa-4f914825325f"
        , "864b2c12-66b1-4783-8a8e-79acdb46442b"
        , "775d00a6-74e3-4eef-9851-238edc9d6ca2"
        , "15860405-f97f-4ca4-b7c1-b09dfdf2579e"
        , "3c7e56c0-da8d-4b62-b04d-76852c9dfe39"
        , "cbec7318-da4d-4090-b882-474197f7f4c8"
        , "fc0229c0-c1d3-458c-ace5-f95f765cc6e1"
        , "2a0717c7-5119-4f0e-b710-7fe486a6ea08"
        , "4a5535ce-7c05-4a5d-86fc-8335ff71fd68"
        , "ee28de26-0601-4de4-8951-a0681e71a8f3"
        , "7f71b4bf-3ff4-451c-b502-4a182f19aaac"
        , "a7a0ebdc-3dcc-40e9-928c-bd5f6163440f"
        , "55f1c742-80b6-4c13-b53a-b6fc8fe4a8c6"
        , "713c6272-4556-4392-b443-85e8b2ac18b0"
        , "83c884a4-2d9c-4072-8901-b8a48ce371d6"
        , "d7310a99-5379-4574-9bbf-beac4c65c9bb"
        , "f7639f8f-4928-4f89-bb43-2509b11dd407"
        , "e836d06c-93e3-4993-8255-b5dedc93f3b6"
        , "23c98974-6845-4b37-a4f5-9e6c80c6ed4b"
        , "f994471a-66a0-494a-b92d-519ca82129db"
        , "05981c48-3623-42aa-9565-d7225573bd1d"
        , "a4ed3ea1-e42b-4ac3-a076-9daa2b585538"
        , "b6b4d3bd-3542-4d37-98ac-4cf5b2b182b8"
        , "181037ad-4e07-4991-8a99-cab5947a5ccb"
        , "ead09a0e-60cc-4a77-9f5b-91d7809843d4"
        , "43b14f52-c01d-4f24-944f-db42144303f8"
        , "b8992979-670f-4e4b-8ac6-db1f94a9bac7"
        , "e50d55a2-dbca-44f7-b547-5ad031509444"
        , "f566bb79-53ca-41a9-bcc2-c517113c999e"
        , "5d5576e1-ca07-4272-b3aa-62cefeba9c7d"
        , "fb993c84-7f72-4678-a8fe-a0198e727085"
        , "644d93e0-f649-4a2b-93a6-c0e2443cec05"
        , "26dc6c68-7083-4b47-8da6-65b7888bb84d"
        , "13f03dec-e245-4700-9abd-122d771a28a9"
        , "b3a9f816-f60b-4312-b419-d302f1849eda"
        , "1ee340a8-a912-4dbf-8f78-38d454e57e73"
        , "8b724b05-8f1b-45fe-830a-1680b4062d21"
        , "4a673f66-1215-46f5-8954-8edf113deaf1"
        , "b7396e7a-e198-4b98-8cd6-b22422e889b0"
        , "9d145dad-b30e-4097-8b74-0a1723b96a9a"
        , "3d4ac0f8-b67a-4021-87a6-584f11adbe87"
        , "d33fb4f3-e5ef-4797-a8d3-a5fad64ef165"
        , "5bf721ad-f793-4806-90aa-ed6e260f32af"
        , "464d9c09-700a-4d7e-aa0c-6575d432f490"
        , "ebefb29d-ebee-405a-9ee9-311ff30fa1d4"
        , "66d9ba62-b040-4045-b607-247ae007728d"
        , "c1b2b38c-3e2e-4baf-81b8-42b04b1f5591"
        , "83b373a8-af9e-40d1-b1cc-59e1587f7e26"
        , "aabec35f-995f-41d6-8bdc-e5461964bdc4"
        , "36df1b0c-6b62-4e4f-a123-33841fe806fe"
        , "a0c8d657-5457-4b1f-9012-a83d2de387bc"
        , "d6fd0d7c-8eb8-4938-901e-10a653cd25d2"
        , "c9c31337-7fe9-405d-9d32-9e4f415ee6c4"
        , "36bcd842-4ad4-4bd9-896c-85665b575bc4"
        , "a2afbe77-4466-4654-b73d-e21186863bec"
        , "c8b57178-d148-4b1a-86ff-feb80559c18d"
        , "003d5ffc-08d1-4c57-811a-a52b9ed85004"
        , "5f6ceb14-00fe-4c16-81fa-4979cd13c5c9"
        , "cfe9e3dc-85d5-43ce-b942-ebc8f63ac61f"
        , "8674f8b9-3d3d-4fbd-8d56-800c7a5bd634"
        , "395516da-2e15-479a-ada9-fe8ebbf55a23"
        , "a1367641-edd3-4bad-a098-17995d99e977"
        , "51ed80b0-0613-4217-83be-a0ef3cd2f3b6"
        , "2a0d5109-4c52-4d84-92f7-c5ad3fef4030"
        , "7232c872-5f29-4518-8d23-abab1b7d26e3"
        , "18ead113-0ac5-4acd-be73-7769cde7efdc"
        , "17b3f839-2039-4f4a-a2f4-11e455f03459"
        , "1605d5d9-1e67-4f56-b133-9266fedd71d5"
        , "986dd256-912c-45b5-958c-a5e4d41e5cc6"
        , "a0ac2b53-11f1-4e93-b53a-5ac956e810cc"
        , "c89b41e0-bbc4-49c9-82f1-b26fdfc4af44"
        , "648075b4-2dfd-4248-a3e9-eb18d6ab2344"
        , "feb22641-7cf7-4f65-bd88-0096025cd569"
        , "bc9a204e-afa6-4cba-8faa-38365f5352e7"
        , "de56a8fb-772f-439d-968f-c872c67447e3"
        , "642d01d9-0a30-454b-9e5f-4b9860cb1029"
        , "fa043389-6934-402e-bdae-dd77a5133d3c"
        , "dcfdd7f2-4bc6-4ed3-94ac-5f1937f895a0"
        , "fda0c085-ccec-4c24-bee5-f0ed2a96463f"
        , "498a383c-e750-4613-a9db-2607756a7957"
        , "cf55ff2c-f4ae-4873-8c53-f7c62a55cbca"
        , "02c70f58-4cc5-4bba-8f2b-4442c96377bb"
        , "b7a8bdd0-24d3-410d-917d-eec587947525"
        , "d9104621-4dca-41bf-be73-82faef0740ad"
        , "693d0a2e-3911-4685-b9a9-da32809a56b6"
        , "8f9667d9-bcc4-4e77-9ee4-ffec86a0fb21"
        , "9793b4f4-dd1c-4adc-9ffc-9fcdcd9ddc1f"
        , "e237861f-0b85-47a9-8295-020311ff8c9b"
        , "80161f6b-4ab5-4093-ac5e-36dc9b85d48b"
        , "76884a05-6411-4de2-a99a-f66b332d6d95"
        , "579e299e-92ce-490a-9cfd-43d91390d0b7"
        , "afa3f693-1fba-4f75-a3cc-4a374dac2647"
        , "909d48bd-89e8-4249-b191-090294cf5012"
        , "919f9502-ba93-48d7-8147-7b148f982402"
        , "705e70d4-401f-4a8e-961e-9d679c1af172"
        , "f66f7296-32b1-466e-9d93-ab8c00e6ca01"
        , "311c0cc2-592b-4c73-af1c-27d5a57bf72d"
        , "e440da7c-22f3-483e-832b-a38f9ff065ca"
        , "13197e5e-b0b6-4ed0-981a-a9ff26f572bd"
        , "683e398b-cdc0-467b-9b4b-f0498a3d4d44"
        , "ca446c48-212b-469c-810c-7e50eaaca4f5"
        , "12cf6228-4c0b-44f2-8610-8ae7d965c713"
        , "05214433-15b3-48af-bfce-e5a980850eac"
        , "0cef97fc-8730-47ea-abf5-69e0da8a9da5"
        , "5c245a52-9a85-44e9-88f8-44ce632a8fd5"
        , "2a8a286c-2fb9-466b-b854-e6398db6fda7"
        , "318b770a-3b2d-4086-a0e8-486e975a8831"
        , "bf544973-7222-4384-9bcd-b0f306aec7b8"
        , "4868d8a0-90f0-4e55-a37f-595af244139c"
        , "1f399bdf-d555-4d84-9c1c-80553bfbac39"
        , "03fc17ee-7504-4232-aac2-6163753f94cc"
        , "5360acb1-bac6-4b47-ada2-b35a397568a8"
        , "65eb5fda-d635-4e3e-a842-f09f241af5ac"
        , "6066613d-401d-45fb-93fe-ab8176559d00"
        , "2cbf24d7-e21a-4ea1-af34-4163f163281d"
        , "8f62ed13-438a-44e2-8851-9c3cc9b292ae"
        , "9d431cd0-c80f-4864-93cc-16cfdac0789e"
        , "73d29f5e-19ca-4b9e-bc99-8620ff96fdd3"
        , "2c43cb57-dc1b-4141-91c4-6f80f70174df"
        , "c451eea5-8e98-41c7-a106-e069431e9627"
        , "85c437f3-16d9-4b9b-a8df-a56e81ad0116"
        , "8920e6a0-4df7-43c5-87c8-1bfb5d10b3a6"
        , "3bd06a9e-910f-4a31-a013-2c71c2b44190"
        , "893ccbd9-def6-4e02-a579-6c4e65485fc0"
        , "f4bd2bcd-3e67-49e9-af52-e8204c4435ae"
        , "5411c6e8-a322-4748-b96e-acd60b193492"
        , "c433ae95-9932-4189-8a6a-624149c22721"
        , "0f29f650-8db8-4f19-9e9c-d8912fc91b11"
        , "c0b0de3a-d8c5-4aa6-8969-19bced41f4ec"
        , "ecf7d127-33e6-439e-a2d8-d8923f1a2e0c"
        , "1af39a9c-c2eb-429d-8c6f-efdaad5ff410"
        , "11436d65-d1e9-49e6-a4d0-ad6b32459ae4"
        , "18000ee7-c987-4071-a0cc-10e8c6b259e4"
        , "321ddff9-9381-422c-a6f5-b09f4d836155"
        , "787e0c0b-2498-467c-9d42-9263b1591e4d"
        , "67117f65-b325-4e66-9d54-7542143ddd11"
        , "fda401bd-e8ae-4fa5-bedc-5ca02e140a61"
        , "411ad978-44ae-4828-964f-0f7030135c45"
        , "0670ff38-b3e4-40b4-b63e-02b8205ec836"
        , "89b7fddb-d456-43f4-a547-95c20daaa23e"
        , "d99919d5-f255-4c2a-9585-766e31e360ae"
        , "e08952d6-4f56-4ca4-9212-f2a7fce81860"
        , "127c56f7-ce28-446d-9aa7-b686a9f60cdd"
        , "d6b95506-a80f-4256-944c-a6f60ce0dfca"
        , "1587f643-f5c2-404a-a8aa-4eb1e3cb49de"
        , "a69a552f-2781-4d0c-8bf3-c05bdad23fd4"
        , "14a03814-797a-4564-ad8b-f00fde110d4d"
        , "f52c3642-f90a-4176-b692-2b2db853a0c6"
        , "5999e12d-b168-48f4-ad2d-1e4689e7e87d"
        , "486eb0d2-02cd-48fd-9bc5-9fdca2056c1f"
        , "027b4d55-567e-49fb-b3f6-67eebeb3655e"
        , "4105f1ec-0475-4eb1-9267-4dc6e27f3ae3"
        , "6a63f965-cceb-4c7b-beae-96c52f975c5d"
        , "297d77c8-362a-41a6-a8e1-ef5d8f5acc15"
        , "b388d7f6-2418-4c61-8d8a-13db19a4c93d"
        , "f0896ae8-bdd1-4587-86ef-af6bf2506b4f"
    };


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        List<CategoryEntity> savedCategories = createCategories();

        for (int i = 0; i < fixedArrCompanyId.length; i++) {
            UUID companyId = getNextFixedUuidCompany();
            List<ProductDto> productDtos = createProductTestDto(savedCategories, companyId);

            // 상품, 재고, 재고기록 등록
            productDtos.forEach(productDto -> {
                this.createProduct(getNextFixedUuid(), productDto);
            });
        }
    }

    //상품 10개 재고 10개 재고기록 10개 생성
    @Transactional
    public ProductEntity createProduct(UUID id, ProductDto productDto) {

        //1. 상품 생성
        ProductEntity productEntity = productDtoToProductEntity(id, productDto);

        //2. 재고엔티티 생성
        ProductStock stock = new ProductStock(productDto.getProductStock());
        ProductStockEntity productStockEntity = ProductStockEntity.create(
            getNextFixedUuid(),
            productEntity,
            stock
        );
        //3. 재고 기록엔티티 생성
        ProductStockHistoryEntity productStockHistoryEntity = ProductStockHistoryEntity.create(
            getNextFixedUuid(),
            productStockEntity,
            null,
            ProductStockHistoryChangeType.INCREASE,
            stock.getCurrentStockQuantity(),
            0,
            stock.getCurrentStockQuantity(),
            null
        );
        //4. 연관관계 설정
        productEntity.addProductStockEntity(productStockEntity);
        productStockEntity.addStockHistory(productStockHistoryEntity);

        return productJpaRepository.save(productEntity);
    }

    private ProductEntity productDtoToProductEntity(UUID id, ProductDto productDto) {

        return ProductEntity.create(
            id,
            categoryRepository.findById(productDto.getCategoryId()).orElse(null),
            productDto.getCompanyId(),
            productDto.getProductName(),
            productDto.getProductPrice(),
            false,
            true
        );
    }

    //10개의 상품 생성
    private List<ProductDto> createProductTestDto(List<CategoryEntity> savedCategories, UUID companyId) {
        List<ProductDto> productDtos = new ArrayList<>();

        int categoryIndex = 0;

        for (String productName : productNames) {
            ProductDto product1 = new ProductDto();
            product1.setProductName(productName);
            product1.setCompanyId(companyId);
            product1.setCategoryId(findCategoryId(savedCategories, categoryValues[categoryIndex++ % 20]));
            double price = (double) random.nextInt(400001);
            product1.setProductPrice(price);
            product1.setProductStock(random.nextInt(201));
            productDtos.add(product1);
        }

        return productDtos;
    }


    //카테고리 20개 생성
    private List<CategoryEntity> createCategories() {

        List<CategoryEntity> categories = new ArrayList<>();

        for (String s : categoryValues) {
            categories.add(CategoryEntity.create(getNextFixedUuid(), s));
        }

        List<CategoryEntity> savedCategories = categoryRepository.saveAll(categories);

        return savedCategories;
    }

    // 카테고리 이름으로 카테고리를 찾는 메서드
    private UUID findCategoryId(List<CategoryEntity> categories, String categoryName) {
        return categories.stream()
            .filter(category -> category.getCategoryName().equals(categoryName))
            .map(CategoryEntity::getCategoryId)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryName));
    }

    // 고정된 UUID 배열에서 하나를 가져오는 메소드
    private UUID getNextFixedUuid() {
        if (uuidIndex >= fixedUuids.length) {
            return UUID.randomUUID();
        }
        return UUID.fromString(fixedUuids[uuidIndex++]);
    }

    private UUID getNextFixedUuidCompany() {
        if (uuidIndex2 >= fixedArrCompanyId.length) {
            return UUID.randomUUID();
        }
        return UUID.fromString(fixedArrCompanyId[uuidIndex2++]);
    }
}
