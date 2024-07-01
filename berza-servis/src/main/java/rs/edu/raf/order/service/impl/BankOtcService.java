package rs.edu.raf.order.service.impl;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.annotations.GeneratedCrudOperation;
import rs.edu.raf.annotations.GeneratedOnlyIntegrationTestable;
import rs.edu.raf.annotations.GeneratedScheduledOperation;
import rs.edu.raf.order.dto.AtmDto;
import rs.edu.raf.order.dto.banka3.FrontendOfferDto;
import rs.edu.raf.order.dto.banka3.MyOfferDto;
import rs.edu.raf.order.dto.banka3.MyStockDto;
import rs.edu.raf.order.dto.banka3.OfferDto;
import rs.edu.raf.order.model.*;
import rs.edu.raf.order.repository.BankOTCStockRepository;
import rs.edu.raf.order.repository.MyOfferRepository;
import rs.edu.raf.order.repository.OfferRepository;
import rs.edu.raf.order.repository.UserStockRepository;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankOtcService {
    private final OfferRepository offerRepository;
    private final UserStockRepository myStockRepository;
    private final BankOTCStockRepository bankOTCStockRepository;
    private final MyOfferRepository myOfferRepository;

    //URL
    private static final String URL_TO_BANK3 =  "https://banka-3-dev.si.raf.edu.rs/exchange-service/api/v1/otcTrade";

    //GET: /getOurStocks
    //dohvatamo sve Stocks koje mi nudimo
    @GeneratedCrudOperation
    public List<MyStockDto> findAllStocks(){
        List<UserStock> myStocks = myStockRepository.findAllByUserId(-1L);
        List<MyStockDto> dtos = new ArrayList<>();
        for(UserStock myStock: myStocks){
            MyStockDto dto = new MyStockDto();
            if(myStock.getPublicQuantity() > 0) {
                dto.setAmount(myStock.getPublicQuantity());
                dto.setTicker(myStock.getTicker());
                dtos.add(dto);
            }
        }
        return dtos;
    }

    //POST: /sendOffer/bank
    //primamo ponude od drugih banaka
    @GeneratedCrudOperation
    public Offer receiveOffer(OfferDto offerDto){
        Offer offer = new Offer();
        offer.setTicker(offerDto.getTicker());
        offer.setAmount(offerDto.getAmount());
        offer.setPrice(offerDto.getPrice());
        offer.setIdBank(offerDto.getIdBank());

        UserStock myStock = myStockRepository.findByUserIdAndTicker(-1l,offer.getTicker());

        //provera da li mi imamo taj Stock
        if(myStock != null && myStock.getPublicQuantity() >= offer.getAmount() && offer.getAmount() >= 0) {
            offer.setOfferStatus(OfferStatus.PROCESSING);
        } else {
            offer.setOfferStatus(OfferStatus.DECLINED);
        }

        offerRepository.save(offer);
        return offer;
    }

    //POST: /offerAccepted/bank/{id}
    //stize poruka da su nam prihvatili ponudu
    @GeneratedCrudOperation
    public boolean offerAccepted(Long id){
        Optional<MyOffer> myOfferOptional = myOfferRepository.findById(id);

        if(myOfferOptional.isPresent()){
            MyOffer myOffer = myOfferOptional.get();
            myOffer.setOfferStatus(OfferStatus.ACCEPTED);

            //provera ukoliko taj Stock ne postoji kod nas
            if(myStockRepository.findByUserIdAndTicker(-1L,myOffer.getTicker()) == null) {
                UserStock myStock = new UserStock();
                myStock.setTicker(myOffer.getTicker());
                myStock.setUserId(-1l);
                myStock.setQuantity(myOffer.getAmount());
                myStock.setPublicQuantity(0);
                myStock.setCurrentAsk(new BigDecimal("1.0"));
                myStock.setCurrentBid(new BigDecimal("1.0"));
//                myStock.setPrivateAmount(0);
//                myStock.setPublicAmount(myOffer.getAmount());
//                myStock.("RSD");
//                double minimumPrice = myOffer.getPrice()/myOffer.getAmount();
//                myStock.(minimumPrice);
                myStockRepository.save(myStock);
            }else {
                UserStock myStock = myStockRepository.findByUserIdAndTicker(-1l,myOffer.getTicker());
                myStock.setQuantity(myStock.getQuantity() + myOffer.getAmount());
                myStockRepository.save(myStock);
            }

            //TODO: skidamo pare sa naseg racuna
            String racunUpdateFundsEndpoint = "http://banka-service:8082/api/racuni/atm";
            Gson gson = new Gson();

            AtmDto atmDto = new AtmDto();
            atmDto.setStanje(BigDecimal.valueOf(-myOffer.getPrice()));
            atmDto.setBrojRacuna(444000000000000022L);


            HttpClient client = HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(URI.create(racunUpdateFundsEndpoint))
                    .header("Content-Type", "application/json")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(gson.toJson(atmDto)))
                    .build();

            try {
                HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response);
            } catch (Exception e) {
                System.out.println("Failed to send balance update to RacunController: " + e);
            }

            myOfferRepository.save(myOffer);
            return true;
        }

        return false;
    }

    //POST: /offerDeclined/bank/{id}
    //stize poruka da su nam odbili ponudu
    @GeneratedCrudOperation
    public boolean offerDeclined(Long id){
        Optional<MyOffer> myOfferOptional = myOfferRepository.findById(id);
        if(myOfferOptional.isPresent()){
            MyOffer myOffer = myOfferOptional.get();
            myOffer.setOfferStatus(OfferStatus.DECLINED);
            myOfferRepository.save(myOffer);
            return true;
        }

        return false;
    }

    ///////////////////////FRONTEND/////////////////////////////////////////////////

    //GET: /getBanksStocks
    //dohvatamo sve Stocks od drugih banaka
    @GeneratedCrudOperation
    public List<BankOTCStock> getAllStocksForBanks(){
        return bankOTCStockRepository.findAll();
    }

    //GET: /getOffers
    //pohvatamo sve ponude koje su nam stigle
    @GeneratedCrudOperation
    public List<Offer> findAllOffers(){
        return offerRepository.findAll();
    }

    //GET: /getOurOffers
    //dohvatamo sve ponude koje smo mi poslali
    @GeneratedCrudOperation
    public List<MyOffer> getMyOffers(){
        return myOfferRepository.findAll();
    }

    //PUT: /refresh
    //pozivi ka banci 3
    @GeneratedCrudOperation
    public void getBankStocks(){
        bankOTCStockRepository.deleteAll();
        getStocksFromBank3();
    }

    @GeneratedOnlyIntegrationTestable
    private void getStocksFromBank3(){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = URL_TO_BANK3 + "/getOurStocks";

            ResponseEntity<List<MyStockDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MyStockDto>>() {
                    });

            if (response.getStatusCode() == HttpStatus.OK){
                List<MyStockDto> dtos = response.getBody();

                for(MyStockDto myStockDto: dtos){
                    BankOTCStock stock = new BankOTCStock();
                    stock.setTicker(myStockDto.getTicker());
                    stock.setAmount(myStockDto.getAmount());
                    bankOTCStockRepository.save(stock);
                }
            }else {
                System.out.println("ne radi banka 3");
            }
        } catch (Exception e){
            System.out.println("ne radi banka 3");
        }
    }

    //POST: /makeOffer
    //sa frontenda nam stize ponuda koju treba proslediti
    @GeneratedOnlyIntegrationTestable
    public boolean makeOffer(FrontendOfferDto frontendOfferDto){
        MyOffer myOffer = new MyOffer();
        myOffer.setTicker(frontendOfferDto.getTicker());
        myOffer.setAmount(frontendOfferDto.getAmount());
        myOffer.setPrice(frontendOfferDto.getPrice());
        myOffer.setOfferStatus(OfferStatus.PROCESSING);
        MyOffer myOffer1 = myOfferRepository.save(myOffer);

        MyOfferDto myOfferDto = new MyOfferDto();
        myOfferDto.setTicker(myOffer1.getTicker());
        myOfferDto.setAmount(myOffer1.getAmount());
        myOfferDto.setPrice(myOffer1.getPrice());
        myOfferDto.setIdBank(myOffer1.getMyOfferId());

        //TODO: promeni na broj svoje banke
        String url = URL_TO_BANK3 + "/sendOffer/bank4";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        try {
            HttpEntity<MyOfferDto> requestEntity = new HttpEntity<>(myOfferDto, headers);

            ResponseEntity<MyOfferDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<MyOfferDto>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK){
                return true;
            }
        }catch (Exception e){
            return false;
        }

        return false;
    }

    //POST: /acceptOffer/{id}
    //kad mi prihvatamo njihovu ponudu
    @GeneratedCrudOperation
    public boolean acceptOffer(Long id){
        Optional<Offer> offer = offerRepository.findById(id);
        if(offer.isPresent()){
            Offer offer1 = offer.get();
            offer1.setOfferStatus(OfferStatus.ACCEPTED);
            offerRepository.save(offer1);

            //smanjujemo kolicinu, uzimamo pare
            UserStock myStock = myStockRepository.findByUserIdAndTicker(-1l,offer1.getTicker());
            myStock.setQuantity(myStock.getQuantity() - offer1.getAmount());
            myStock.setPublicQuantity(myStock.getPublicQuantity() - offer1.getAmount());
            myStockRepository.save(myStock);

            //TODO: dodajemo pare na nas racun
            String racunUpdateFundsEndpoint = "http://banka-service:8082/api/racuni/atm";
            Gson gson = new Gson();

            AtmDto atmDto = new AtmDto();
            atmDto.setStanje(BigDecimal.valueOf(offer1.getPrice()));
            atmDto.setBrojRacuna(444000000000000022L);


            HttpClient client = HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(URI.create(racunUpdateFundsEndpoint))
                    .header("Content-Type", "application/json")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(gson.toJson(atmDto)))
                    .build();

            try {
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                System.out.println("Failed to send balance update to RacunController: " + e);
            }

            offerRepository.save(offer1);
            return true;
        }

        return false;
    }

    //POST: /declineOffer/{id}
    //kad mi odbijemo njihovu ponudu
    @GeneratedCrudOperation
    public boolean declineOffer(Long id){
        Optional<Offer> offer = offerRepository.findById(id);
        if(offer.isPresent()){
            Offer offer1 = offer.get();
            offer1.setOfferStatus(OfferStatus.DECLINED);

            offerRepository.save(offer1);
            return true;
        }

        return false;
    }

    //DELETE: /deleteMyOffer/id
    //kada treba neka nasu ponuda da obrisemo iz baze
    @GeneratedCrudOperation
    public boolean deleteMyOffer(Long id){
        Optional<MyOffer> myOfferOptional = myOfferRepository.findById(id);
        if(myOfferOptional.isPresent()) {
            MyOffer myOffer = myOfferOptional.get();
            myOfferRepository.delete(myOffer);
            return true;
        }

        return false;
    }

    //DELETE: /deleteOffer/id
    //kada treba neka tudju ponuda da obrisemo iz baze
    @GeneratedCrudOperation
    public boolean deleteOffer(Long id){
        Optional<Offer> offerOptional = offerRepository.findById(id);
        if(offerOptional.isPresent()) {
            Offer offer = offerOptional.get();
            offerRepository.delete(offer);
            return true;
        }

        return false;
    }

    @Scheduled(fixedRate = 10000)
    @GeneratedScheduledOperation
    private void sendAcceptedOffers() {
        List<Offer> offers = offerRepository.findAllByOfferStatus(OfferStatus.ACCEPTED);
        if (!offers.isEmpty()) {
            for (Offer offer : offers) {
                String url = URL_TO_BANK3 + "/offerAccepted/bank4/" + offer.getIdBank();

                try {
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> response = restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            new HttpEntity<>(offer),
                            new ParameterizedTypeReference<String>() {
                            });

                    if (response.getStatusCode() == HttpStatus.OK) {
                        //zavrsavamo sa ponudom
                        offer.setOfferStatus(OfferStatus.FINISHED_ACCEPTED);
                        offerRepository.save(offer);
                    }
                } catch (Exception e) {
                    return;
                }
            }
        }
    }

    @Scheduled(fixedRate = 10000)
    @GeneratedScheduledOperation
    private void sendDeclinedOffers(){
        List<Offer> offers = offerRepository.findAllByOfferStatus(OfferStatus.DECLINED);
        if(!offers.isEmpty()){
            for(Offer offer : offers){
                String url = URL_TO_BANK3 + "/offerDeclined/bank4/" + offer.getIdBank();

                try {
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> response = restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            new HttpEntity<>(offer),
                            new ParameterizedTypeReference<String>() {
                            });

                    if (response.getStatusCode() == HttpStatus.OK){
                        //zavrsavamo sa ponudom
                        offer.setOfferStatus(OfferStatus.FINISHED_DECLINED);
                        offerRepository.save(offer);
                    }
                }catch (Exception e){
                    return;
                }
            }
        }
    }

}