#!/bin/bash

rm -rf card_images

mkdir card_images

function gg {
  let x=1
  mkdir card_images
  mkdir card_images/$3
  while [ $x -le $2 ]
  do
    suffix=""
    web="/HD"
    if [ "$#" -eq 4 ]
      then
      suffix=\_$4
      web=""
    fi
    echo "downloading $1$web/$x.jpg"
    
    wget -q $1$web/$x.jpg -O card_images/$3/$3\_$x$suffix.jpg
    if [ $? -gt 0 ]
      then
     
      if [ "$#" -ne 4 ]
        then

        echo "downloading $1/$x.jpg error try without HD"
        wget -q $1/$x.jpg -O card_images/$3/$3\_$x$suffix.jpg
        if [ $? -gt 0 ]
          then
          echo "redownloading $1/$x.jpg"
          rm card_images/$3/$3\_$x$suffix.jpg
          wget -q $1/$x.jpg -O card_images/$3/$3\_$x$suffix.jpg
          if [ $? -gt 0 ]
            then
            rm card_images/$3/$3\_$x$suffix.jpg
            #cp nocard.jpeg card_images/$3/$3\_$x$suffix.jpg
          fi

        fi
      else

        echo "redownloading $1/$x.jpg"
        rm card_images/$3/$3\_$x$suffix.jpg
        wget -q $1/$x.jpg -O card_images/$3/$3\_$x$suffix.jpg
        if [ $? -gt 0 ]
          then
          rm card_images/$3/$3\_$x$suffix.jpg
          #cp nocard.jpeg card_images/$3/$3\_$x$suffix.jpg
        fi


      fi

    fi
    let x=$x+1
  done
  zip -r $3$suffix.zip card_images
  rm -r card_images
}


gg http://www.pokecardex.com/serie/AQ 182 aqu
gg http://serebii.net/card/aquapolis 182 aqu us
#-------------------------------------------------------
gg http://www.pokecardex.com/serie/BA 102 bas
gg http://serebii.net/card/base 102 bas us
#---------------------------------------------
gg http://www.pokecardex.com/serie/BW 115 blw
gg http://serebii.net/card/blackwhite 115 blw us
#gg http://www.pokecardex.com/images/promo/BlackStarDPus 56 bsdp
##gg http://www.pokecardex.com/images/nexistepas.gif 130 bs2
##gg http://www.pokecardex.com/images/promo 17 Bbwp
##gg http://www.pokecardex.com/images/nexistepas.gif 12 Bbxt
#---------------------------------------------------------
gg http://www.pokecardex.com/serie/LM 93 cdl
gg http://serebii.net/card/exlegendmaker 93 cdl us
gg http://www.pokecardex.com/serie/CL 106 cl
######http://serebii.net/card/emergingpowers/
#--------------------------------------------------------
gg http://www.pokecardex.com/serie/DP 130 dp1
#gg http://serebii.net/card/diamondandpearl 130 dp1 us
gg http://www.pokecardex.com/serie/MT 124 dp2
gg http://www.pokecardex.com/serie/SW 132 dp3
gg http://www.pokecardex.com/serie/GEn 106 dp4
gg http://www.pokecardex.com/serie/MD 100 dp5
gg http://www.pokecardex.com/serie/LA 147 dp6
gg http://www.pokecardex.com/serie/SF 106 dp7
#---------------------------------------------------------
gg http://www.pokecardex.com/serie/DR 100 dra
gg http://serebii.net/card/exdragon 100 dra us
#---------------------------------------------------------
gg http://www.pokecardex.com/serie/DX 108 dxs
gg http://serebii.net/card/exdeoxys 108 dxs us
#---------------------------------------------------------
gg http://www.pokecardex.com/serie/DS 114 ed
gg http://serebii.net/card/exdeltaspecies 114 ed us
#---------------------------------------------------------
gg http://www.pokecardex.com/serie/EM 107 eme
gg http://serebii.net/card/exemerald 107 eme us
gg http://www.pokecardex.com/serie/EX 165 exp
#---------------------------------------------------------
gg http://www.pokecardex.com/serie/UF 145 fc
gg http://serebii.net/card/exunseenforces 145 fc us
#---------------------------------------------------------
gg http://www.pokecardex.com/serie/HP 111 fh
gg http://serebii.net/card/exholonphantoms 111 fh us
#------------------------------------------------------
gg http://www.pokecardex.com/serie/FO 62 fos
gg http://serebii.net/card/fossil 62 fos us
#------------------------------------------------------
gg http://www.pokecardex.com/serie/CG 100 gdc
gg http://serebii.net/card/excrystalguardians 100 gdc us
#------------------------------------------------------
gg http://www.pokecardex.com/serie/PK 108 gdp
gg http://serebii.net/card/expowerkeepers 108 gdp us
#--------------------------------------------------------
gg http://www.pokecardex.com/serie/HGSS 124 gs
gg http://serebii.net/card/heartgoldsoulsilver 124 gs us
#-----------------------------------------------------------
gg http://www.pokecardex.com/serie/GC 132 gyc
gg http://serebii.net/card/gymchallenge 132 gyc us
#-----------------------------------------------------------
gg http://www.pokecardex.com/serie/GH 132 gyh
gg http://serebii.net/card/gymheroes 132 gyh us
##gg http://www.pokecardex.com/images/promo 25 hsp
#-----------------------------------------------------------
gg http://www.pokecardex.com/serie/DF 101 idd
gg http://serebii.net/card/exdragonfrontiers 101 idd us
#-------------------------------------------------------
gg http://www.pokecardex.com/serie/JU 64 jun
gg http://serebii.net/card/jungle 64 jun us
gg http://www.pokecardex.com/serie/LC 110 lco
#-------------------------------------------------------
gg http://www.pokecardex.com/serie/HL 102 lo
gg http://serebii.net/card/exhiddenlegends 102 lo us
#gg http://www.pokecardex.com/images/nexistepas 12 mc1
##gg 40 NBS
#----------------------------------------------------------
gg http://www.pokecardex.com/serie/DE 113 nde
gg http://serebii.net/card/neodestiny 113 nde us
#--------------------------------------------------------
gg http://www.pokecardex.com/serie/DI 75 ndi
gg http://serebii.net/card/neodiscovery 75 ndi us
#--------------------------------------------------------
gg http://www.pokecardex.com/serie/GE 111 nge
gg http://serebii.net/card/neogenesis 111 nge us
#-------------------------------------------------------
gg http://www.pokecardex.com/serie/RE 66 nre
gg http://serebii.net/card/neorevelation 66 nre us
gg http://www.pokecardex.com/serie/RR 120 pl2
gg http://www.pokecardex.com/serie/SV 153 pl3
gg http://www.pokecardex.com/serie/AR 111 pl4
#gg http://www.pokecardex.com/images/promo/POP_Series1 17 pop1
#gg http://www.pokecardex.com/images/promo/POP_Series2 17 pop2
#gg http://www.pokecardex.com/images/promo/POP_Series3 17 pop3
#gg http://www.pokecardex.com/images/promo/POP_Series4 17 pop4
#gg http://www.pokecardex.com/images/promo/POP_Series5 17 pop5
#gg http://www.pokecardex.com/images/promo/POP_Series6 17 pop6
#gg http://www.pokecardex.com/images/promo/POP_Series7 17 pop7
#gg http://www.pokecardex.com/images/promo/POP_Series8 17 pop8
#gg http://www.pokecardex.com/images/promo/POP_Series9 17 pop9
#---------------------------------------------------------------
gg http://www.pokecardex.com/serie/PL 133 pt
gg http://serebii.net/card/arceus 133 pt us
#---------------------------------------------------------------
gg http://www.pokecardex.com/serie/RS 109 rs
gg http://serebii.net/card/exrubyandsapphire 109 rs us
#-----------------------------------------------------------
gg http://www.pokecardex.com/serie/RFVF 116 rfvf
gg http://serebii.net/card/exfireredandleafgreen 116 rfvf us
##gg http://www.pokecardex.com/images/nexistepas.gif 16 rum
##gg http://www.pokecardex.com/images/nexistepas.gif 18 si
#---------------------------------------------------------------
gg http://www.pokecardex.com/serie/SK 182 sky
gg http://serebii.net/card/skyridge 182 sky us
#---------------------------------------------------------------
gg http://www.pokecardex.com/serie/TDS 100 tds
gg http://serebii.net/card/exsandstorm 100 tds us
##gg http://www.pokecardex.com/images/nexistepas.gif 20 tk1
##gg http://www.pokecardex.com/images/nexistepas.gif 24 tk2
##gg http://www.pokecardex.com/images/nexistepas.gif 11 tk3l
##gg http://www.pokecardex.com/images/nexistepas.gif 12 tk3m
##gg http://www.pokecardex.com/images/nexistepas.gif 60 tk4
#---------------------------------------------------------------
gg http://www.pokecardex.com/serie/TM 103 tm
gg http://serebii.net/card/triumphant 103 tm us
#---------------------------------------------------------------
gg http://www.pokecardex.com/serie/TMTA 97 tmta
gg http://serebii.net/card/exteammagmavsteamaqua 97 tmta us
#----------------------------------------------------------------
gg http://www.pokecardex.com/serie/TR 83 tr
gg http://serebii.net/card/teamrocket 83 tr us
#---------------------------------------------------------------
gg http://www.pokecardex.com/serie/TRR 111 trr
gg http://serebii.net/card/exteamrocketreturns 111 trr us
#---------------------------------------------------------------
gg http://www.pokecardex.com/serie/UD 91 ud
gg http://serebii.net/card/undaunted/ 91 ud us
#---------------------------------------------------------------
gg http://www.pokecardex.com/serie/UL 96 ul
#gg http://serebii.net/card/unleashed 96 ul us
##gg http://www.pokecardex.com/images/promo 9 wbp
##gg http://www.pokecardex.com/images/promo 54 wotc

