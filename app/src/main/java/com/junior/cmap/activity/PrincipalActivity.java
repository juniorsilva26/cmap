package com.junior.cmap.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.junior.cmap.R;
import com.junior.cmap.config.ConfiguracaoFirebase;
import com.junior.cmap.fragments.BoletimFragment;
import com.junior.cmap.fragments.CalendarioFragment;
import com.junior.cmap.fragments.DocumentosFragment;
import com.junior.cmap.fragments.MensagensFragment;
import com.junior.cmap.model.Usuario;

import de.hdodenhof.circleimageview.CircleImageView;


public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("cmap/usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getKey().equals(ConfiguracaoFirebase.getFireBaseAuth().getCurrentUser().getUid())) {
                        View headerView = navigationView.getHeaderView(0);
                        CircleImageView imageHeader = headerView.findViewById(R.id.imageProfileHeader);
                        TextView textNome = headerView.findViewById(R.id.txtNomeHeader);
                        TextView textEmail = headerView.findViewById(R.id.txtEmailHeader);

                        Usuario u = ds.getValue(Usuario.class);
                        textNome.setText(u.getNomeCompleto());
                        textEmail.setText(u.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        init();
    }

    public void init() {
        getSupportActionBar().setTitle("Notificações");
        context = this;

        MensagensFragment mensagensFragment = new MensagensFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, mensagensFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.menuPesquisar){
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.caixaMensagens) {
            setActionBarTitle("Notificações");
            MensagensFragment mensagensFragment = new MensagensFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, mensagensFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.proximosEventos) {
            setActionBarTitle("Próximos Eventos");
        } else if (id == R.id.calendarioAcademico) {
            setActionBarTitle("Calendário Acadêmico");
            CalendarioFragment calendarioFragment = new CalendarioFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, calendarioFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.forumPais) {
            setActionBarTitle("Fórum de Pais");
        } else if (id == R.id.boletim) {
            setActionBarTitle("Boletim");
            BoletimFragment boletimFragment = new BoletimFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, boletimFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.frequencia) {
            setActionBarTitle("Frequência");

        } else if (id == R.id.documentos){
            setActionBarTitle("Solicitar Documentos");
            DocumentosFragment documentosFragment = new DocumentosFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, documentosFragment);
            fragmentTransaction.commit();
        }else if (id == R.id.menuSair) {
            try{
                FirebaseAuth auth = ConfiguracaoFirebase.getFireBaseAuth();
                auth.signOut();
                startActivity(new Intent(PrincipalActivity.this, MainActivity.class));
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void abrirTela(Class tela){
        Intent intent = new Intent(context, tela);
        startActivity(intent);
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }
}
