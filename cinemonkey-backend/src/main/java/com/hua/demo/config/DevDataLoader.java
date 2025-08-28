package com.hua.demo.config;

import com.hua.demo.movie.Movie;
import com.hua.demo.movie.MovieServiceImpl;
import com.hua.demo.reservation.ReservationServiceImpl;
import com.hua.demo.showing.Showing;
import com.hua.demo.showing.ShowingServiceImpl;
import com.hua.demo.showing.State;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Component
@ConditionalOnProperty(prefix = "app", name = "seed", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
public class DevDataLoader implements CommandLineRunner{
    private final MovieServiceImpl movieService;
    private final ShowingServiceImpl showingService;
    private final ReservationServiceImpl reservationService;
    @Override
    public void run(String... args) throws Exception {
        //Demo Movies
        movieService.saveMovie(
                new Movie(
                        "Banana Wars",
                        "https://cinemonkey.com/media/BANANAWARS.webp",
                        "Pongo Lucas",
                        162,
                        "Σε ένα μακρινό σύμπαν, όπου η εξουσία και το πεπρωμένο ισορροπούν σε κάθε γωνιά της γαλαξιακής ζούγκλας, ξεδιπλώνεται μια επική ιστορία που συνδυάζει το δράμα του διαστήματος με την ατάκα της ζωής των μαιμούδων. Σε αυτή την εκδοχή του κλασικού saga, οι πρωταγωνιστές μας είναι δυναμικές μαιμούδες-μαχητές που παλεύουν για την ελευθερία του γαλαξία και για το απόλυτο μυστικό της – τη μαγική μπανάνα.\n"+
                                "\n"+"Η ιστορία ακολουθεί την ορμή του νεαρού ηρώα, του Λεοναρντ, ο οποίος μαζί με τους αχώριστους σύντροφούς του, αντιμετωπίζει μια σκοτεινή δύναμη που επιδιώκει να κατακτήσει την εξουσία και να διεκδικήσει το απόλυτο έμβλημα δύναμης: την ατέρμονη τροφή της ζούγκλας. Καθώς αναζητούν θρυλικές συμμαχίες και εξερευνούν μυστηριώδεις πλανήτες, κάθε μάχη μετατρέπεται σε ένα μοναδικό ταξίδι γεμάτο δράση, χιούμορ και ατέλειωτες περιπέτειες που μόνο οι μαιμούδες μπορούν να προσφέρουν.\n"+ "\n"+
                                "\"Banana Wars\" είναι ένα εκρηκτικό μίγμα επιστημονικής φαντασίας και κωμωδίας, όπου οι παραδοσιακοί ήρωες αντικαθίστανται από καρικατούρες της ζούγκλας, δίνοντας νέα πνοή σε έναν κλασικό διαστημικό μύθο. Ετοιμαστείτε να απογειωθείτε σε ένα ταξίδι γεμάτο εκπλήξεις, όπου η μπανάνα γίνεται το σύμβολο της ελπίδας και της επανάστασης!",
                        "Sci-Fi, Adventure, Action, Fantasy"
                ));
        movieService.saveMovie(
                new Movie(
                        "Monkey Business",
                        "https://cinemonkey.com/media/MB.webp",
                        "Μπονόμπος Πιθηκόπουλος",
                        194,
                        "Σε μια πόλη όπου οι κανόνες της ζούγκλας έχουν ανατραπεί, δύο αδίστακτες μαιμούδες, ο Τζίμπα και ο Μπαρούκ, αναλαμβάνουν τα ηνία ως παλαιά σχολικά γκάνγκστερ. Με αστείρευτη εξυπνάδα και σκληρότητα, αυτοί οι πρωταγωνιστές ξεχωρίζουν στον κόσμο της εγκληματικής σφαίρας, προσφέροντας έναν συνδυασμό χιούμορ και δράσης που σαρώνει τα στενά σοκάκια της αστικής ζωής.\n" +"\n" +
                                "Η ταινία \"Monkey Business\" συνδυάζει την απρόβλεπτη πλοκή με αξέχαστες σκηνές δράσης, μεταφέροντας τον θεατή σε ένα μοναδικό ταξίδι όπου η φιλία, η επιβίωση και ο παλμός της ρετρό εποχής συγκρούονται με τις σύγχρονες προκλήσεις. Ανακαλύψτε ένα κόσμο όπου το έγκλημα γίνεται τέχνη και κάθε κίνηση υποδηλώνει μια ιστορία πίσω από τη μάσκα του κινδύνου.",
                        "Action Comedy"
                ));
        movieService.saveMovie(
                new Movie(
                        "Gorilla of Wallstreet",
                        "https://cinemonkey.com/media/GOFWS.png",
                        "James Gorilla",
                        102,
                        "Σε μια ασυνήθιστη στροφή στη συνηθισμένη ιστορία του χρηματιστηρίου, \"Gorilla of Wallstreet\" παρουσιάζει την απίστευτη ανέλιξη ενός γορίλλας που κατάφερε να κερδίσει εκατομμύρια, αξιοποιώντας και καταχράζοντας τα κενά του αμερικανικού νομικού συστήματος.\n" +"\n" +
                                "Η ταινία μας μεταφέρει στον κόσμο των χρηματοοικονομικών συναλλαγών, όπου ο πρωταγωνιστής, ένας έξυπνος και αδίστακτος γορίλλα, παρακάμπτει τους κανόνες με μια ακραία και κωμική ματιά στην επιδίωξη του πλούτου. Με πρωτοφανή στρατηγική και αδίστακτη επιχειρηματικότητα, ο Gorilla μας εισβάλλει στις καρδιές των Wall Street, δημιουργώντας ένα ακραίο μείγμα νομικής αταξίας και χρηματοπιστωτικής σκανδαλογραφίας.\n" +"\n" +
                                "Μέσα από απίστευτες καταστάσεις, σάτιρα και μαύρο χιούμορ, η ταινία αναδεικνύει πώς ακόμα και ο πιο απροσδόκητος ήρωας μπορεί να μεταμορφώσει το παρασκήνιο των αμερικανικών νόμων σε ένα πεδίο για επενδύσεις και διασκέδαση. \"Gorilla of Wallstreet\" είναι μια μοναδική κωμική περιπέτεια που αμφισβητεί τα όρια του συνηθισμένου και προσφέρει μια δόση ανεξήγητης κωμωδίας στον κόσμο των μεγάλων χρημάτων.",
                        "Comedy"
                ));
        movieService.saveMovie(
                new Movie(
                        "Bonobos in the Moon",
                        "https://cinemonkey.com/media/BONOBOSINTHEMOON.webp",
                        "Monkey Brothers",
                        143,
                        "Σε ένα απόμακρο, σκοτεινό φεγγάρι, μια διαστημική βάση γίνεται το σκηνικό ενός αληθινά εφιαλτικού τρόμου. Ένα πλήρωμα από μπονομπούς αστροναύτες, εξοπλισμένοι για την εξερεύνηση του διαστήματος, βρίσκονται παγιδευμένοι μέσα στην εγκαταλειμμένη αυτή βάση, όπου η απομόνωση και ο απροσδόκητος κίνδυνος συναντούν το ανεξήγητο.\n" +"\n" +
                                "Καθώς οι πρωταγωνιστές μας προσπαθούν να επιβιώσουν, αντιλαμβάνονται σταδιακά ότι δεν είναι μόνοι. Σκιές που κινούνται χωρίς προέλευση, περίεργοι ήχοι που διαπερνούν τους ψυχρούς διαδρόμους και ανεξήγητα φαινόμενα δημιουργούν μια ατμόσφαιρα παγερούς αγωνίας και τρόμου. Ο φόβος του άγνωστου δέχεται κάθε τους προσπάθεια να διαφύγουν, καθώς το μυστήριο της βάσης αποκαλύπτει σκοτεινά μυστικά και μια παρουσία που ξεπερνά κάθε λογική.\n" + "\n" +
                                "\"BONOBOSINTHEMOON\" είναι ένα απόκοσμο horror έργο που συνδυάζει το στοιχείο της επιστημονικής φαντασίας με τον υπερφυσικό τρόμο, μετατρέποντας την απομόνωση του διαστήματος σε ένα πεδίο αγωνίας όπου ο φόβος γίνεται καθημερινότητα. Ετοιμαστείτε να βιώσετε έναν διαστημικό εφιάλτη, όπου το φεγγάρι κρύβει μυστικά και οι μπονομποί δεν είναι μόνοι στο σκοτάδι!",
                        "Horror, Sci-Fi"
                ));
        movieService.saveMovie(
                new Movie(
                        "Dancing with Bonobos",
                        "https://cinemonkey.com/media/DWB.webp",
                        "Pete Ape",
                        96,
                        "Βυθιστείτε στην εποχή της λάμψης, της χρωματιστής μόδας και των άκρως εκρηκτικών ρυθμών των 80s με την ιστορία ενός πίθηκου που άλλαξε τα δεδομένα της σκηνής του χορού. Σε μια δεκαετία όπου κάθε βήμα στο πατώμα της dance floor μετράει, ο πρωταγωνιστής μας, ένας εξαιρετικά χαρισματικός πίθηκος, καταφέρνει να κατακτήσει τις καρδιές του κοινού με το μοναδικό του στυλ και την απρόβλεπτη ενέργειά του.\n" +"\n" +
                                "Από τα ζωντανά clubs μέχρι τις μεγάλες σκηνές, ο ήρωάς μας χορεύει με πάθος και ακαταμάχητη φιλοδοξία, δημιουργώντας μια νέα τάση στον κόσμο του dance. Με κάθε του κίνηση, αναμειγνύει την αυθορμητικότητα της ζωής στο ζούγκλα με τη λάμψη και την πολυτέλεια της εποχής των 80s, αποδεικνύοντας πως ο χορός δεν γνωρίζει όρια ούτε για την ανθρώπινη ψυχή ούτε για την πονηρή καρδιά ενός πίθηκου.\n" +"\n" +
                                "\"Dancing with Bonobos\" είναι μια ζωντανή, γεμάτη ρυθμό και χιούμορ ταινία που αναδεικνύει το πάθος για τον χορό και την ελευθερία της έκφρασης, αφήνοντάς σας να ταξιδέψετε πίσω στον μαγικό κόσμο της δεκαετίας που όλα έλαβαν διαφορετική διάσταση—όπου ακόμη και οι πίθηκοι αναδεικνύονται ως αστέρια του dance floor!",
                        "Comedy"
                ));
        movieService.saveMovie(
                new Movie(
                        "Urangutango",
                        "https://cinemonkey.com/media/URANGUTANGO.webp",
                        "Αμπου Μαϊμουδίδης",
                        131,
                        "Σε έναν κόσμο όπου η αγάπη δεν ακολουθεί κανόνες και οι περιπέτειες βρίσκουν το δρόμο τους στα πιο απρόσμενα μέρη, \"Uragotango\" σε ταξιδεύει σε ένα ξεκαρδιστικό honey moon γεμάτο ρομαντισμό και απίθανες συγκινήσεις.\n" +"\n" +
                                "Οι πρωταγωνιστές μας, ένας γοητευτικός ουραγκοταγκός και μια δυναμική θηλίκη ρινοκερό, ξεκινούν την κοινή τους ζωή με μια παραμυθένια απόδραση, όπου η αλληλεπίδραση των αντιθέτων τους δημιουργεί μια μοναδική χημεία. Μέσα από αστείρευτα αστεία, τρυφερές στιγμές και απρόβλεπτες καταστάσεις, αναδεικνύεται η ομορφιά της αγάπης που ξεπερνά όλα τα εμπόδια—even όταν αυτά είναι τόσο ασυνήθιστα όσο το να αγαπηθούν ένας ουραγκοταγκός και μια ρινοκερό!\n" +"\n" +
                                "\"Uragotango\" είναι μια ρομαντική κωμωδία που συνδυάζει το παράδοξο με το τρυφερό, προσφέροντας μια ιστορία που θα σε κάνει να γελάς, να ονειρεύεσαι και να πιστεύεις πως κάθε αγάπη, όσο ασυνήθιστη κι αν φαίνεται, έχει τη δική της μαγεία.",
                        "Romantic Comedy"
                ));
        //Demo Showings
        showingService.saveShowing(
                new Showing(
                        0,
                        State.FINISHED,
                        "03/16/2023 20:30:00",
                        1
                ));
        showingService.saveShowing(
                new Showing(
                        0,
                        State.SCHEDULED,
                        "03/16/2023 20:30:00",
                        2
                ));
        showingService.saveShowing(
                new Showing(
                        0,
                        State.SCHEDULED,
                        "03/16/2023 20:30:00",
                        3
                ));
        showingService.saveShowing(
                new Showing(
                        0,
                        State.SCHEDULED,
                        "03/16/2023 20:30:00",
                        4
                ));
        showingService.saveShowing(
                new Showing(
                        0,
                        State.SCHEDULED,
                        "03/16/2023 20:30:00",
                        5
                ));
        showingService.saveShowing(
                new Showing(
                        0,
                        State.SCHEDULED,
                        "03/16/2023 20:30:00",
                        6
                ));
        showingService.saveShowing(
                new Showing(
                        0,
                        State.SCHEDULED,
                        "04/16/2023 20:30:00",
                        1
                ));
        showingService.saveShowing(
                new Showing(
                        0,
                        State.SCHEDULED,
                        "04/16/2023 20:30:00",
                        2
                ));
        showingService.saveShowing(
                new Showing(
                        0,
                        State.SCHEDULED,
                        "04/16/2023 20:30:00",
                        3
                ));
        showingService.saveShowing(
                new Showing(
                        0,
                        State.SCHEDULED,
                        "04/16/2023 20:30:00",
                        4
                ));
        showingService.saveShowing(
                new Showing(
                        0,
                        State.SCHEDULED,
                        "04/16/2023 20:30:00",
                        5
                ));
        showingService.saveShowing(
                new Showing(
                        0,State.SCHEDULED,
                        "04/16/2023 20:30:00",
                        6
                ));
        //Adding movies to showings
        showingService.addMovieToShowing(1,"Banana Wars");
        showingService.addMovieToShowing(2,"Monkey Business");
        showingService.addMovieToShowing(3,"Gorilla of Wallstreet");
        showingService.addMovieToShowing(4,"Bonobos in the Moon");
        showingService.addMovieToShowing(5,"Urangutango");
        showingService.addMovieToShowing(6,"Dancing with Bonobos");
        showingService.addMovieToShowing(7,"Banana Wars");
        showingService.addMovieToShowing(8,"Monkey Business");
        showingService.addMovieToShowing(9,"Gorilla of Wallstreet");
        showingService.addMovieToShowing(10,"Bonobos in the Moon");
        showingService.addMovieToShowing(11,"Urangutango");
        showingService.addMovieToShowing(12,"Dancing with Bonobos");
        //Demo Reservations
        reservationService.saveReservation("e4940726-b4d0-4bf2-a287-f821d2505f62","jackdoe@gmail.com",1);
        reservationService.saveReservation("e4940726-b4d0-4bf2-a287-f821d2505f62","janedoe@gmail.com",3);
        reservationService.saveReservation("e4940726-b4d0-4bf2-a287-f821d2505f62","janedoe@gmail.com",5);
        reservationService.saveReservation("e4940726-b4d0-4bf2-a287-f821d2505f62","janedoe@gmail.com",7);
        reservationService.saveReservation("e4940726-b4d0-4bf2-a287-f821d2505f62","johndoe@hotmail.com",1);
        reservationService.saveReservation("e4940726-b4d0-4bf2-a287-f821d2505f62","johndoe@hotmail.com",2);
        reservationService.saveReservation("e4940726-b4d0-4bf2-a287-f821d2505f62","johndoe@hotmail.com",4);
        reservationService.saveReservation("e4940726-b4d0-4bf2-a287-f821d2505f62","janedoe@gmail.com",6);
        reservationService.saveReservation("e4940726-b4d0-4bf2-a287-f821d2505f62","janedoe@gmail.com",8);
    }
}
