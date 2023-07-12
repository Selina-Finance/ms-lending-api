/*
 * Copyright 2022 Selina Finance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.selina.lending.api.dto.dip.request;

public enum Nationality {
    AFGHANISTAN("Afghanistan"),
    ALAND_ISLANDS("Aland Islands"),
    ALBANIA("Albania"),
    ALGERIA("Algeria"),
    ANDORRA("Andorra"),
    ANGOLA("Angola"),
    ANGUILLA("Anguilla"),
    ANTARCTICA("Antarctica"),
    ANTIGUA_AND_BARBUDA("Antigua and Barbuda"),
    ARGENTINA("Argentina"),
    ARMENIA("Armenia"),
    ARUBA("Aruba"),
    AUSTRALIA("Australia"),
    AUSTRIA("Austria"),
    AZERBAIJAN("Azerbaijan"),
    BAHAMAS("Bahamas"),
    BAHRAIN("Bahrain"),
    BANGLADESH("Bangladesh"),
    BARBADOS("Barbados"),
    BELARUS("Belarus"),
    BELGIUM("Belgium"),
    BELIZE("Belize"),
    BENIN("Benin"),
    BERMUDA("Bermuda"),
    BHUTAN("Bhutan"),
    BOLIVIA_PLURINATIONAL_STATE("Bolivia, Plurinational State of"),
    BONAIRE_SINT_EUSTATIUS_AND_SABA("Bonaire, Sint Eustatius and Saba"),
    BOSNIA_AND_HERZEGOVINA("Bosnia and Herzegovina"),
    BOTSWANA("Botswana"),
    BOUVET_ISLAND("Bouvet Island"),
    BRAZIL("Brazil"),
    BRITISH_INDIAN_OCEAN_TERRITORY("British Indian Ocean Territory"),
    BRUNEI_DARUSSALAM("Brunei Darussalam"),
    BULGARIA("Bulgaria"),
    BURKINA_FASO("Burkina Faso"),
    BURUNDI("Burundi"),
    CAMBODIA("Cambodia"),
    CAMEROON("Cameroon"),
    CANADA("Canada"),
    CAPE_VERDE("Cape Verde"),
    CAYMAN_ISLANDS("Cayman Islands"),
    CENTRAL_AFRICAN_REPUBLIC("Central African Republic"),
    CHAT("Chad"),
    CHILE("Chile"),
    CHINA("China"),
    CHRISTMAS_ISLAND("Christmas Island"),
    COCOS_KEELING_ISLANDS("Cocos (Keeling) Islands"),
    COLOMBIA("Colombia"),
    COMOROS("Comoros"),
    CONGO("Congo"),
    CONGO_DEMOCRATIC_REPUBLIC("Congo, the Democratic Republic of the"),
    COOK_ISLANDS("Cook Islands"),
    COSTA_RICA("Costa Rica"),
    COTE_DIVOIRE("Cote d’Ivoire"),
    CROATIA("Croatia"),
    CUBA("Cuba"),
    CURUCAO("Curaçao"),
    CYPRUS("Cyprus"),
    CZECH_REPUBLIC("Czech Republic"),
    DENMARK("Denmark"),
    DJIBOUTI("Djibouti"),
    DOMINICA("Dominica"),
    DOMINICAN_REPUBLIC("Dominican Republic"),
    ECUADOR("Ecuador"),
    EGYPT("Egypt"),
    EL_SALVADOR("El Salvador"),
    EQUATORIAL_GUINEA("Equatorial Guinea"),
    ERITREA("Eritrea"),
    ESTONIA("Estonia"),
    ETHIOPIA("Ethiopia"),
    FALKLAND_ISLANDS("Falkland Islands (Malvinas)"),
    FAROE_ISLANDS("Faroe Islands"),
    FIJI("Fiji"),
    FINLAND("Finland"),
    FRANCE("France"),
    FRENCH_GUIANA("French Guiana"),
    FRENCH_POLYNESIA("French Polynesia"),
    FRENCH_SOUTHERN_TERRITORIES("French Southern Territories"),
    GABON("Gabon"),
    GAMBIA("Gambia"),
    GEORGIA("Georgia"),
    GERMANY("Germany"),
    GHANA("Ghana"),
    GIBRALTAR("Gibraltar"),
    GREECE("Greece"),
    GREENLAND("Greenland"),
    GRENADA("Grenada"),
    GUADELOUPE("Guadeloupe"),
    GUATEMALA("Guatemala"),
    GUERNSEY("Guernsey"),
    GUINEA("Guinea"),
    GUINEA_BISSAU("Guinea-Bissau"),
    GUYANA("Guyana"),
    HAITI("Haiti"),
    HEARD_ISLAND_MCDONALD_ISLANDS("Heard Island and McDonald Islands"),
    HOLY_SEE_VATICAN_CITY_STATE("Holy See (Vatican City State)"),
    HONDURAS("Honduras"),
    HUNGARY("Hungary"),
    ICELAND("Iceland"),
    INDIA("India"),
    INDONESIA("Indonesia"),
    IRAN("Iran, Islamic Republic of"),
    IRAQ("Iraq"),
    IRELAND("Ireland"),
    ISLE_OF_MAN("Isle of Man"),
    ISRAEL("Israel"),
    ITALY("Italy"),
    JAMAICA("Jamaica"),
    JAPAN("Japan"),
    JERSEY("Jersey"),
    JORDAN("Jordan"),
    KAZAKHSTAN("Kazakhstan"),
    KENYA("Kenya"),
    KIRIBATI("Kiribati"),
    KOREA_DEMOCRATIC_PEOPLES_REPUBLIC("Korea, Democratic People’s Republic of"),
    KOREA("Korea, Republic of"),
    KUWAIT("Kuwait"),
    KYRGYZSTAN("Kyrgyzstan"),
    LAO_PEOPLES_DEMOCRATIC_REPUBLIC("Lao People’s Democratic Republic"),
    LATVIA("Latvia"),
    LEBANON("Lebanon"),
    LESOTHO("Lesotho"),
    LIBERIA("Liberia"),
    LIBYAN_ARAB_JAMAHIRIYA("Libyan Arab Jamahiriya"),
    LIECHTENSTEIN("Liechtenstein"),
    LITHUANIA("Lithuania"),
    LUXEMBOURG("Luxembourg"),
    MACAO("Macao"),
    MACEDONIA("Macedonia, the former Yugoslav Republic of"),
    MADAGASCAR("Madagascar"),
    MALAWI("Malawi"),
    MALAYSIA("Malaysia"),
    MALDIVES("Maldives"),
    MALI("Mali"),
    MALTA("Malta"),
    MARTINIQUE("Martinique"),
    MAURITANIA("Mauritania"),
    MAURITIUS("Mauritius"),
    MAYOTTE("Mayotte"),
    MEXICO("Mexico"),
    MOLDOVO("Moldova, Republic of"),
    MANACO("Monaco"),
    MONGOLIA("Mongolia"),
    MONTENEGRO("Montenegro"),
    MONTSERRAT("Montserrat"),
    MOROCCO("Morocco"),
    MOZAMBIQUE("Mozambique"),
    MYANMAR("Myanmar"),
    NAMIBIA("Namibia"),
    NAURU("Nauru"),
    NEPAL("Nepal"),
    NETHERLANDS("Netherlands"),
    NEW_CALEDONIA("New Caledonia"),
    NEW_ZEALAND("New Zealand"),
    NICARAGUA("Nicaragua"),
    NIGER("Niger"),
    NIGERIA("Nigeria"),
    NIUE("Niue"),
    NORFOLK_ISLAND("Norfolk Island"),
    NORWAY("Norway"),
    OMAN("Oman"),
    PAKISTAN("Pakistan"),
    PALESTINE("Palestine"),
    PANAMA("Panama"),
    PAPUA_NEW_GUINEA("Papua New Guinea"),
    PARAGUAY("Paraguay"),
    PERU("Peru"),
    PHILIPPINES("Philippines"),
    PITCAIRN("Pitcairn"),
    POLAND("Poland"),
    PORTUGAL("Portugal"),
    QATAR("Qatar"),
    REUNION("Reunion"),
    ROMANIA("Romania"),
    RUSSIAN_FEDERATION("Russian Federation"),
    RWANDA("Rwanda"),
    SAINT_BARTHELEMY("Saint Barthélemy"),
    SAINT_HELENA_ASCENSION_AND_TRISTAN_DA_CUNHA("Saint Helena, Ascension and Tristan da Cunha"),
    SAINT_KITTS_AND_NEVIS("Saint Kitts and Nevis"),
    SAINT_LUCIA("Saint Lucia"),
    SAINT_MARTIN_FRENCH_PART("Saint Martin (French part)"),
    SAINT_PIERRE_AND_MIQUELON("Saint Pierre and Miquelon"),
    SAINT_VINCENT_AND_GRENADINES("Saint Vincent and the Grenadines"),
    SAMOA("Samoa"),
    SAN_MARINO("San Marino"),
    SAO_TOME_AND_PRINCIPE("Sao Tome and Principe"),
    SAUDI_ARABIA("Saudi Arabia"),
    SENEGAL("Senegal"),
    SERBIA("Serbia"),
    SEYCHELLES("Seychelles"),
    SIERRA_LEONE("Sierra Leone"),
    SINGAPORE("Singapore"),
    SINT_MAARTEN_DUTCH_PART("Sint Maarten (Dutch part)"),
    SLOVAKIA("Slovakia"),
    SLOVENIA("Slovenia"),
    SOLOMON_ISLANDS("Solomon Islands"),
    SOMALIA("Somalia"),
    SOUTH_AFRICA("South Africa"),
    SOUTH_GEORGIA_AND_SOUTH_SANDWICH_ISLANDS("South Georgia and the South Sandwich Islands"),
    SOUTH_SUDAN("South Sudan"),
    SPAIN("Spain"),
    SRI_LANKA("Sri Lanka"),
    SUDAN("Sudan"),
    SURINAME("Suriname"),
    SVALBARD_AND_JAN_MAYEN("Svalbard and Jan Mayen"),
    SWAZILAND("Swaziland"),
    SWEDEN("Sweden"),
    SWITZERLAND("Switzerland"),
    SYRIAN_ARAB_REPUBLIC("Syrian Arab Republic"),
    TAIWAN("Taiwan"),
    TAJIKISTAN("Tajikistan"),
    TANZANIA("Tanzania, United Republic of"),
    THAILAND("Thailand"),
    TIMOR_LESTE("Timor-Leste"),
    TOGO("Togo"),
    TOKELAU("Tokelau"),
    TONGA("Tonga"),
    TRINIDAD_AND_TOBAGO("Trinidad and Tobago"),
    TUNISIA("Tunisia"),
    TURKEY("Turkey"),
    TURKMENISTAN("Turkmenistan"),
    TURKS_AND_CAICOS_ISLANDS("Turks and Caicos Islands"),
    TUVALU("Tuvalu"),
    UGANDA("Uganda"),
    UKRAINE("Ukraine"),
    UNITED_ARAB_EMIRATES("United Arab Emirates"),
    UNITED_KINGDOM("United Kingdom"),
    UNITED_STATES("United States"),
    URUGUAY("Uruguay"),
    UZBEKISTAN("Uzbekistan"),
    VANUATU("Vanuatu"),
    VENEZUELA("Venezuela, Bolivarian Republic of"),
    VIETNAM("Vietnam"),
    VIRGIN_ISLANDS_BRITISH("Virgin Islands, British"),
    WALLIS_AND_FUTUNA("Wallis and Futuna"),
    WESTERN_SAHARA("Western Sahara"),
    YEMEN("Yemen"),
    ZAMBIA("Zambia"),
    ZIMBABWE("Zimbabwe"),
    UNKNOWN("Unknown");

    private final String value;

    Nationality(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
