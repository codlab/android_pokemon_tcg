#!/bin/bash

rm -rf card_images

mkdir card_images

deactivate_fr=1
deactivate_us=0

function gg {
  if [ "$#" -eq 4 ]
    then
    if [ $deactivate_us -eq 1 ]
      then
      return
    fi
  elif [ $deactivate_fr -eq 1 ]
    then
    return
  fi

  let x=1
  mkdir -p $3
  pfolder=$3$suffix/card_images
  folder=$pfolder/$3

  if [ "$#" -eq 4 ]
    then
    suffix=\_$4
    pfolder=$3$suffix/card_images
    folder=$pfolder/$3
  fi
  mkdir -p $pfolder
  mkdir -p $folder
  
  while [ $x -le $2 ]
  do
    suffix=""
    web="/HD"
    if [ "$#" -eq 4 ]
      then
      web=""
      suffix=\_$4
    fi
    echo "downloading $1$web/$x.jpg"
    
    wget --timeout=15 -q $1$web/$x.jpg -O $folder/$3\_$x$suffix.jpg
    if [ $? -gt 0 ]
      then
     
      if [ "$#" -ne 4 ]
        then

        echo "downloading $1/$x.jpg error try without HD"
        wget --timeout=15 -q $1/$x.jpg -O $folder/$3\_$x$suffix.jpg
        if [ $? -gt 0 ]
          then
          echo "redownloading $1/$x.jpg"
          rm $folder/$3\_$x$suffix.jpg
          wget --timeout=15 -q $1/$x.jpg -O $folder/$3\_$x$suffix.jpg
          if [ $? -gt 0 ]
            then
            rm $folder/$3\_$x$suffix.jpg
            #cp nocard.jpeg $folder/$3\_$x$suffix.jpg
          fi

        fi
      else

        echo "redownloading $1/$x.jpg"
        rm $folder/$3\_$x$suffix.jpg
        wget --timeout=15 -q $1/$x.jpg -O $folder/$3\_$x$suffix.jpg
        if [ $? -gt 0 ]
          then
          rm $folder/$3\_$x$suffix.jpg
          #cp nocard.jpeg card_images/$3/$3\_$x$suffix.jpg
        fi


      fi

    fi
    let x=$x+1
  done
  zip -r $3$suffix.zip $pfolder
  rm -r $3
}

#bas
function bas {
  gg http://www.pokecardex.com/serie/BA 102 bas
  gg http://69.65.41.131/card/base 102 bas us
}

#jun
function jun {
  gg http://www.pokecardex.com/serie/JU 64 jun
  gg http://69.65.41.131/card/jungle 64 jun us
}

#fos
function fos {
  gg http://www.pokecardex.com/serie/FO 62 fos
  gg http://69.65.41.131/card/fossil 62 fos us
}

#tr
function tr {
  gg http://www.pokecardex.com/serie/TR 83 tr
  gg http://69.65.41.131/card/teamrocket 83 tr us
}

#nge
function nge {
  gg http://www.pokecardex.com/serie/GE 111 nge
  gg http://69.65.41.131/card/neogenesis 111 nge us
}

#ndi
function ndi {
  gg http://www.pokecardex.com/serie/DI 75 ndi
  gg http://69.65.41.131/card/neodiscovery 75 ndi us
}

#nre
function nre {
  gg http://www.pokecardex.com/serie/RE 66 nre
  gg http://69.65.41.131/card/neorevelation 66 nre us
}

#nde
function nde {
  gg http://www.pokecardex.com/serie/DE 113 nde
  gg http://69.65.41.131/card/neodestiny 113 nde us
}

#exp
function exp {
  gg http://www.pokecardex.com/serie/EX 165 exp
  gg http://69.65.41.131/card/expedition/ 165 exp us
}

#aqu
function aqu {
  gg http://www.pokecardex.com/serie/AQ 182 aqu
  gg http://69.65.41.131/card/aquapolis 182 aqu us
}

#sky
function sky {
  gg http://www.pokecardex.com/serie/SK 182 sky
  gg http://69.65.41.131/card/skyridge 182 sky us
}

#gyh
function gyh {
  gg http://www.pokecardex.com/serie/GH 132 gyh
  gg http://69.65.41.131/card/gymheroes 132 gyh us
}

#gyc
function gyc {
  gg http://www.pokecardex.com/serie/GC 132 gyc
  gg http://69.65.41.131/card/gymchallenge 132 gyc us
}

#si

#rs
function rs {
  gg http://www.pokecardex.com/serie/RS 109 rs
  gg http://69.65.41.131/card/exrubyandsapphire 109 rs us
}

#tds
function tds {
  gg http://www.pokecardex.com/serie/TDS 100 tds
  gg http://69.65.41.131/card/exsandstorm 100 tds us
}

#dra
function dra {
  gg http://www.pokecardex.com/serie/DR 100 dra
  gg http://69.65.41.131/card/exdragon 100 dra us
}

#rfvf
function rfvf {
  gg http://www.pokecardex.com/serie/RFVF 116 rfvf
  gg http://69.65.41.131/card/exfireredandleafgreen 116 rfvf us
}

#tmta
function tmta {
  gg http://www.pokecardex.com/serie/TMTA 97 tmta
  gg http://69.65.41.131/card/exteammagmavsteamaqua 97 tmta us
}

#trr
function trr {
  gg http://www.pokecardex.com/serie/TRR 111 trr
  gg http://69.65.41.131/card/exteamrocketreturns 111 trr us
}

#lo
function lo {
  gg http://www.pokecardex.com/serie/HL 102 lo
  gg http://69.65.41.131/card/exhiddenlegends 102 lo us
}

#dxs
function dxs {
  gg http://www.pokecardex.com/serie/DX 108 dxs
  gg http://69.65.41.131/card/exdeoxys 108 dxs us
}

#eme
function eme {
  gg http://www.pokecardex.com/serie/EM 107 eme
  gg http://69.65.41.131/card/exemerald 107 eme us
}

#fc
function fc {
  gg http://www.pokecardex.com/serie/UF 145 fc
  gg http://69.65.41.131/card/exunseenforces 145 fc us
}

#ed
function ed {
  gg http://www.pokecardex.com/serie/DS 114 ed
  gg http://69.65.41.131/card/exdeltaspecies 114 ed us
}

#cdl
function cdl {
  gg http://www.pokecardex.com/serie/LM 93 cdl
  gg http://69.65.41.131/card/exlegendmaker 93 cdl us
}

#fh
function fh {
  gg http://www.pokecardex.com/serie/HP 111 fh
  gg http://69.65.41.131/card/exholonphantoms 111 fh us
}

#gdc
function gdc {
  gg http://www.pokecardex.com/serie/CG 100 gdc
  gg http://69.65.41.131/card/excrystalguardians 100 gdc us
}

#idd
function idd {
  gg http://www.pokecardex.com/serie/DF 101 idd
  gg http://69.65.41.131/card/exdragonfrontiers 101 idd us
}

#gdp
function gdp {
  gg http://www.pokecardex.com/serie/PK 108 gdp
  gg http://69.65.41.131/card/expowerkeepers 108 gdp us
}

#dp7
function dp7 {
  gg http://www.pokecardex.com/serie/SF 106 dp7
  gg http://69.65.41.131/card/stormfront 106 dp7 us
}

#dp6
function dp6 {
  gg http://www.pokecardex.com/serie/LA 147 dp6
  gg http://69.65.41.131/card/legendsawakened 147 dp6 us
}

#dp5
function dp5 {
  gg http://www.pokecardex.com/serie/MD 100 dp5
  gg http://69.65.41.131/card/majesticdawn 100 dp5 us
}

#dp4
function dp4 {
  gg http://www.pokecardex.com/serie/GEn 106 dp4
  gg http://69.65.41.131/card/greatencounters 106 dp4 us
}

#dp3
function dp3 {
  gg http://www.pokecardex.com/serie/SW 132 dp3
  gg http://69.65.41.131/card/secretwonders 132 dp3 us
}

#dp2
function dp2 {
  gg http://www.pokecardex.com/serie/MT 124 dp2
  gg http://69.65.41.131/card/mysterioustreasures 124 dp2 us
}

#dp1
function dp1 {
  gg http://www.pokecardex.com/serie/DP 130 dp1
  gg http://69.65.41.131/card/diamondandpearl 130 dp1 us
}

#pt
function pt {
  gg http://www.pokecardex.com/serie/PL 133 pt
  gg http://69.65.41.131/card/arceus 133 pt us
}

#pl2
function pl2 {
  gg http://www.pokecardex.com/serie/RR 120 pl2
  gg http://69.65.41.131/card/risingrivals 120 pl2 us
}

#pl3
function pl3 {
  gg http://www.pokecardex.com/serie/SV 153 pl3
  gg http://69.65.41.131/card/supremevictors 153 pl3 us
}

#pl4
function pl4 {
  gg http://www.pokecardex.com/serie/AR 111 pl4
  #TODO add arceus values
  gg http://69.65.41.131/card/arceus 111 pl4 us
}

#gs
function gs {
  gg http://www.pokecardex.com/serie/HGSS 124 gs
  gg http://69.65.41.131/card/heartgoldsoulsilver 124 gs us
}

#ul
function ul {
  gg http://www.pokecardex.com/serie/UL 96 ul
  gg http://69.65.41.131/card/unleashed 96 ul us
}

#ud
function ud {
  gg http://www.pokecardex.com/serie/UD 91 ud
  gg http://69.65.41.131/card/undaunted/ 91 ud us
}

#tm
function tm {
  gg http://www.pokecardex.com/serie/TM 103 tm
  gg http://69.65.41.131/card/triumphant 103 tm us
}

#cl
function cl {
  gg http://www.pokecardex.com/serie/CL 106 cl
}

#blw
function blw {
  gg http://www.pokecardex.com/serie/BW 115 blw
  gg http://69.65.41.131/card/blackwhite 115 blw us
}

#ep
function ep {
  gg http://www.pokecardex.com/serie/EP 98 ep
  gg http://69.65.41.131/card/emergingpowers 98 ep us
}

#nv
function nv {
  gg http://www.pokecardex.com/serie/NV 101 nv
  gg http://69.65.41.131/card/noblevictories 101 nv us
}

#nd
function nd {
  gg http://www.pokecardex.com/serie/ND 99 nd
  gg http://69.65.41.131/card/nextdestinies 99 nd us
}

#de
function de {
  gg http://www.pokecardex.com/serie/DEX 108 de
  gg http://69.65.41.131/card/darkexplorers 108 de us
}

#dre
function dre {
  gg http://www.pokecardex.com/serie/DRX 124 dre
  gg http://69.65.41.131/card/dragonsexalted 124 dre us
}

#bc
function bc {
  gg http://www.pokecardex.com/serie/BCR 149 bc
  gg http://69.65.41.131/card/boundariescrossed 149 bc us
}

#ps
function ps {
  gg http://www.pokecardex.com/serie/PLS 135 ps
  gg http://69.65.41.131/card/plasmastorm 135 ps us
}

#pf
function pf {
  gg http://www.pokecardex.com/serie/PLF 116 pf
  gg http://69.65.41.131/card/plasmafreeze 116 pf us
}

#pb
function pb {
  #TODO fr values
  gg http://www.pokecardex.com/serie/PLB 101 pb
  gg http://69.65.41.131/card/plasmablast 101 pb us
}

#plt
function plt {
  #TODO fr values
  gg http://www.pokecardex.com/serie/LTR 113 plt
  gg http://69.65.41.131/card/legendarytreasures 113 plt us
}

#xy
function xy {
  gg http://www.pokecardex.com/serie/XY 146 xy
  gg http://69.65.41.131/card/xy 146 xy us
}


function part1 {
  bas
  jun
  fos
  tr
  nge
  ndi
  nre
  nde
  exp
  aqu
}
part1 &

function part2 {
  sky
  gyh
  gyc
  rs
  tds
  dra
  rfvf
  tmta
  trr
  lo
}
part2 &

function part3 {
  dxs
  eme
  fc
  ed
  cdl
  fh
  gdc
  idd
  gdp
  dp7
}
part3 &

function part4 {
  dp6
  dp5
  dp4
  dp3
  dp2
  dp1
  pt
  pl2
  pl3
  pl4
}
part4 &

function part5 {
  gs
  ul
  ud
  tm
  cl
  blw
  ep
  nv
  nd
  de
  dre
  bc
  ps
  pf
  pb
  plt
  xy
}
part5 &

#downloading values
#}
exit 0


