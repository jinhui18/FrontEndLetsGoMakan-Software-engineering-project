package com.example.application;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingTermsAndConditions extends AppCompatActivity {

    private TextView textView_terms_and_conditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_terms_and_condition);

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView_terms_and_conditions = findViewById(R.id.textView_terms_and_conditions);
        textView_terms_and_conditions.setText("This website is owned and operate by Let’s Go Makan! Pte Ltd, a company incorporated in the Republic of Singapore (“Let’s Go Makan!”, “we” or “us”). By using these services provided by Let’s Go Makan! including any mobile application or any tool made available by Let’s Go Makan! (“Let’s Go Makan! Services”) you are agreeing to be bound by the following terms and conditions (\"Terms of Use\") and Privacy Policy. From time to time, Let’s Go Makan! may publish terms and conditions for the use of specific products, tools or functionalities that must be read in conjunction with these Terms of Use and Privacy Policy.\n" +
        "\n" +
        "A. Terms of Use\n" +
        "If you are using this website or any Let’s Go Makan! Services for your individual non- commercial purposes, you must be 13 years old or older, and if you are below 21 years of age, you must obtain the permission of a parent or legal guardian.\n" +
        "\u200D\n" +
        "If you are using this website or any Let’s Go Makan! Services for commercial purposes on behalf of a business, partnership, company or any other type of entity, you must be, and you hereby represent that you are, a duly authorised representative of the relevant entity.\n" +
        "\u200D\n" +
        "You are responsible for any activity that occurs under your Let’s Go Makan! account.You are responsible for keeping your password secure.\n" +
        "\n" +
        "You must not abuse, harass, threaten, impersonate or intimidate other Let’s Go Makan! users.\n" +
        "\n" +
        "You cannot impersonate other persons, businesses, companies or entities.\n" +
        "\n" +
        "You may not use Let’s Go Makan!’s website(s) or the Let’s Go Makan! Services for any illegal, immoral, unethical or unauthorized purpose. You must comply with all local laws regarding online conduct and acceptable content that may apply to you. You may not post any type of unlawful, inflammatory, obscene, defamatory, offensive or objectionable content, including accusations of illegal activity or food hygiene offences.\n" +
        "\n" +
        "You are solely responsible for your conduct and any data, text, information, screen names, graphics, photos, profiles, audio and video clips, links, “likes”, emoticons, works of authorship or any other material (\"Content\") that you submit, post and display on Let’s Go Makan!’s website(s) or the Let’s Go Makan! Services.\n" +
        "\n" +
        "If the Content depicts any person other than yourself, you must have permission from that person or, if that person is a minor, permission from that person’s parent or legal guardian, before you post the Content.\n" +
        "\n" +
        "You must not modify, copy, adapt, undermine the security, operations or stability of, or hack any of Let’s Go Makan!’s website(s) or the Let’s Go Makan! Services or modify another website so as to falsely imply that it is associated with Let’s Go Makan!.\n" +
        "\n" +
        "You must not access Let’s Go Makan!'s private API by any other means other than the Let’s Go Makan! application itself.\n" +
        "\n" +
        "You must not crawl, scrape, or otherwise cache any content from Let’s Go Makan!’s website(s) or the Let’s Go Makan! Services including but not limited to user profiles and photos, content uploaded or generated by other Let’s Go Makan! users or content provided by Let’s Go Makan!’s commercial partners, if any.\n" +
        "\n" +
        "You must not create or submit unwanted email or comments to any Let’s Go Makan! user or member, or harass or create a nuisance for any other person or organisation.\n" +
        "\n" +
        "You must not use web URLs in your name or post any material in the nature of advertising, promotional or publicity materials without prior written consent from Let’s Go Makan!.\n" +
        "\n" +
        "You must not transmit any worms or viruses or any code of a destructive nature.\n" +
        "\n" +
        "You must not post any Content that is malicious, false, misleading or deceptive.\n" +
        "\n" +
        "You must not, when using Let’s Go Makan!’s website(s) or the Let’s Go Makan! Services, violate any laws in your jurisdiction (including but not limited to copyright laws).\n" +
        "\n" +
        "You must not violate another party’s rights.\n" +
        "\n" +
        "Violation of any of the provisions of these Terms of Use or the Privacy Policy will result in the termination of your Let’s Go Makan! account. While Let’s Go Makan! prohibits certain conduct and Content on its website(s) and the Let’s Go Makan! Services, you understand and agree that Let’s Go Makan! cannot be and is not responsible for the Content posted and displayed on its web site(s) or the Let’s Go Makan! Services and you nonetheless may be exposed to such material and that you use Let’s Go Makan!’s website(s) or the Let’s Go Makan! Services at your own risk.\n" +
        "\n" +
        "You must not provide us with any information you deem to be confidential. Any information you provide to us, by whatever channel, shall be deemed non-confidential.\n" +
        "\n" +
        "If you have any vested interest such as any affiliation with any business reviewed, you must disclose the same to Let’s Go Makan!. We reserve the sole right to remove any such Content.\n" +
        "\n" +
        "General Conditions\n" +
        "We reserve the right to modify or terminate Let’s Go Makan!’s website(s) or the Let’s Go Makan! Services for any reason, without notice at any time. Notwithstanding any other provision of these Terms of Use or Privacy Policy, we also reserve the right to suspend or terminate your account at any time without prior notice to you or any other party.\n" +
        "\n" +
        "We reserve the right to alter these Terms of Use or Privacy Policy at any time. If the alterations constitute a material change to these Terms of Use or Privacy Policy, we will notify you using such means which we deem to be reasonable. What constitutes a \"material change\" will be determined at our sole discretion, in good faith and using common sense and reasonable judgement.\n" +
        "\n" +
        "We reserve the right to refuse service to anyone for any reason at any time.\n" +
        "\n" +
        "We reserve the right to force forfeiture of any username that becomes inactive, violates any trademark, trade name or any other intellectual property right, breaches these Terms of Use, is obscene or inappropriate, or may mislead other users.\n" +
        "\n" +
        "We may, but have no obligation to, remove any Content. In particular, we may at our sole discretion, remove Content and accounts posting Content that we determine in our sole discretion to be unlawful, abusive, vulgar, indecent, offensive, threatening, libellous, defamatory, obscene, off-topic or otherwise objectionable or violates any party's intellectual property rights or these Terms of Use or Privacy Policy.\n" +
        "\n" +
        "We generally do not remove any negative reviews unless, in our sole opinion, warranted by the circumstances. We reserve the right to reclaim usernames on behalf of businesses or individuals that hold legal claim or trademark on those usernames.\n" +
        "\n" +
        "Proprietary Rights in Content on Let’s Go Makan!\n" +
        "All intellectual property rights in Let’s Go Makan!’s website, Let’s Go Makan!’s Services and any trade mark, trade name, logo, slogan, and content used by Let’s Go Makan! on and in relation to Let’s Go Makan!’s website(s) and Let’s Go Makan!’s Services (“Let’s Go Makan!’s Intellectual Property”) belong solely to Let’s Go Makan! or its licensors (if any). You acquire no rights to Let’s Go Makan!’s Intellectual Property, and only have the right to use the same subject to your compliance with these Terms of Use.\n" +
        "\n" +
        "We do NOT claim ANY ownership rights in Content that you post on or using the Let’s Go Makan! Services.\n" +
        "\n" +
        "By displaying or publishing (\"posting\") any Content on or using any Let’s Go Makan! Services, you hereby grant to Let’s Go Makan! a non-exclusive, fully paid and royalty-free, perpetual, worldwide, limited license to sub-license, use, create derivative works from, modify, delete from, distribute, share, store, add to, publicly perform, publicly display, reproduce, promote and translate all or any part of any Content, including without limitation to distribute part or all of the Content in any media format through any media channel whether owned or operated by Let’s Go Makan! or third party partners of Let’s Go Makan! and the right to attribute Content used by Let’s Go Makan! to you, PROVIDED HOWEVER that Content not shared publicly will not be distributed outside the Let’s Go Makan! Services. For the avoidance of doubt, Let’s Go Makan!'s license stated in this paragraph shall survive even if you choose to delete your account.\n" +
        "\n" +
        "You acknowledge and agree that Let’s Go Makan! may add to or combine your Content with content provided by other users, licensees of Let’s Go Makan! or Let’s Go Makan! owned content to generate lists or new content.\n" +
        "\n" +
        "You agree that Let’s Go Makan! owns all right, title, and interest in any compilation, collective work, or other derivative work created by Let’s Go Makan! using or incorporating Content.\n" +
        "\n" +
        "Some of the Let’s Go Makan! Services are supported by advertising revenue and may display advertisements and promotions, and you hereby agree that Let’s Go Makan! may place such advertising and promotions on the Let’s Go Makan! Services or on, about, or in conjunction with your Content. The manner, mode and extent of such advertising and promotions are subject to change without specific notice to you.\n" +
        "\n" +
        "You represent and warrant that: (i) you own the Content posted by you on or through Let’s Go Makan!’s website(s) and/or the Let’s Go Makan! Services or otherwise have the right to grant the licenses set forth in these Terms of Use, (ii) the posting and use of your Content on or through Let’s Go Makan!’s websites and/or the Let’s Go Makan! Services does not violate the privacy rights, publicity rights, copyrights, contract rights, intellectual property rights or any other rights of any person, and (iii) the posting of your Content on Let’s Go Makan!’s website(s) and/or using Let’s Go Makan!’s Services does not result in a breach of contract between you and a third party. You agree to pay and be solely responsible for all royalties, fees, and any other monies owing to any person by reason of Content you post on or through Let’s Go Makan!’s website(s) and/or the Let’s Go Makan! Services.\n" +
        "\n" +
        "Let’s Go Makan!’s website(s) and the Let’s Go Makan! Services contain content owned, created or licensed by Let’s Go Makan!, including but not limited to the trade name and trade mark “Let’s Go Makan!” and any logos or slogans used by Let’s Go Makan! from time to time (\"Let’s Go Makan! Content\"). Let’s Go Makan! Content is protected by copyright, trademark, patent, trade secret and other laws, and Let’s Go Makan! owns and retains all rights in the Let’s Go Makan! Content , Let’s Go Makan!’s website(s) and the Let’s Go Makan! Services. Let’s Go Makan! hereby grants you a limited, revocable, non-sublicensable license to reproduce and display the Let’s Go Makan! Content (excluding any software code) solely for your personal use in connection with viewing Let’s Go Makan!’s website(s) and using the Let’s Go Makan! Services.\n" +
        "\n" +
        "In addition to Let’s Go Makan! Content, the Let’s Go Makan! Services contains Content of Let’s Go Makan! users and other Let’s Go Makan! licensors. Except as expressly provided in these Terms of Use or Privacy Policy, you may not copy, modify, translate, publish, broadcast, transmit, distribute, perform, display, or sell any Content appearing on or through the Let’s Go Makan! Services. You acknowledge and agree that attributing any third-party Content to the third party does not absolve or release you from any legal obligation to obtain the consent of the owner of the Content before you use such Content in any manner. You acknowledge and agree that Let’s Go Makan! reserves the right at all times to require you to stop any use of another party’s Content. If another party’s Content is found on a website, blog, application or publication, whether online or offline, that you control, operate, publish or represent, then you hereby acknowledge that you are solely responsible for the breach of that party’s rights, and that Let’s Go Makan! has the right to require you to stop using that party’s Content, and you agree to comply with such request immediately.\n" +
        "\n" +
        "You acknowledge and agree that Let’s Go Makan! may perform any action it may deem necessary to offer the Let’s Go Makan! website(s) and the Let’s Go Makan! Services, including but not limited to transcoding and/or reformatting Content to allow its use throughout the Let’s Go Makan! Services.\n" +
        "\n" +
        "Although Let’s Go Makan!’s website(s) and the Let’s Go Makan! Services are normally available, there will be occasions when Let’s Go Makan!’s websites or the Let’s Go Makan! Services will be interrupted for scheduled maintenance or upgrades, for emergency repairs, or due to failure of telecommunications links and equipment that are beyond the control of Let’s Go Makan!. Also, although Let’s Go Makan! will normally only delete Content that violates these Terms of Use or Privacy Policy, Let’s Go Makan! reserves the right to delete any Content for any reason, without prior notice. Deleted Content may be stored by Let’s Go Makan! in order to comply with certain legal obligations and is not retrievable without a valid court order. Consequently, Let’s Go Makan! encourages you to maintain your own backup of your Content. In other words, Let’s Go Makan! is not a backup service. Let’s Go Makan! will not be liable to you for any modification, suspension, or discontinuation of Let’s Go Makan!’s website(s) or the Let’s Go Makan! Services, or the loss of any Content.\n" +
        "\n" +
        "If you wish to report infringements of copyright by any person, kindly email us at feedback@Let’s Go Makan!.com\n" +
        "\n" +
        "If you have any claim to make against Let’s Go Makan! you must do so within one year after such claim arose; failing which, your claim is permanently barred.\n" +
        "\n" +
        "Legal Disclaimers\n" +
        "You agree to indemnify, hold harmless, and defend Let’s Go Makan! and its affiliated companies, and their respective predecessors, successors, and assigns, and all of their respective current and former officers, directors, shareholders, agents, and employees (the “Indemnified Parties”) from any and all actions, causes of action, suits, proceedings, claims, and/or demands of any third party (and all resulting judgments, bona fide settlements, penalties, damages, losses, liabilities, costs, and expenses, including without limitation, reasonable attorneys’ fees and costs), which arise out of: (a) your actual or alleged breach of these Terms of Use and Privacy Policy, (b) your negligent acts or omissions, or (c) any third party claim, action, or demand related to your use of Let’s Go Makan!’s website(s) or the Let’s Go Makan! Services.\n" +
        "\u200D\n" +
        "Let’s Go Makan! provides its website(s) and the Let’s Go Makan! Services “as is” and hereby disclaims all warranties and representations, whether express, implied, or otherwise, including the warranties of merchantability or fitness for a particular purpose, non-infringement, title, or quiet enjoyment and from viruses and other harmful devices. Let’s Go Makan! does not warrant that Let’s Go Makan!’s website(s) or the Let’s Go Makan! Services are error-free or will operate without interruption. You use Let’s Go Makan!’s website(s) or the Let’s Go Makan! Services entirely at your own risk.\n" +
        "\u200D\n" +
        "Under no circumstances shall we, our affiliates, or any of our respective directors, officers, employees, agents, or contractors be liable to you for any direct or indirect, special, incidental, punitive, exemplary, multiplied or consequential damages including lost profits even if we have been advised of the possibility of such damages. \n" +
        "If your use of Let’s Go Makan! website(s) and the Let’s Go Makan! Services service results in any interruption, impairment or loss of, or your need to service, repair or correct, any software, equipment, systems or data, you agree to assume, and hold us harmless from, any costs and losses in connection therewith.\n" +
        "\u200D\n" +
        "Let’s Go Makan!’s website(s) or the Let’s Go Makan! Services may, from time to time, contain links or other means of access to other sites that are not owned or controlled by us. Please note that this Terms of Use and Privacy Policy do not apply to the practices of third parties that Let’s Go Makan! does not own or control, or individuals that we do not employ or manage. If you wish to know more about the manner in which third parties regulate their relationship with you, kindly study the terms of use of their services or contact them directly.\n" +
        "\u200D\n" +
        "In addition to links, Let’s Go Makan!’s website(s) or the Let’s Go Makan! Services may, from time to time, enable you to contact or communicate with third parties such as restaurants. Let’s Go Makan! is not responsible for your relationship with any such third parties. Your sole recourse in the event of any dispute with such third parties is directly with such third parties.\n" +
        "\n" +
        "From time to time as we deem appropriate, we may allow access to or advertise certain third-party product or service providers (\"Third Parties”) from whom you may purchase certain goods or services or make reservations. Transactions for these goods and services or the making of reservations may or may not be concluded on Let’s Go Makan!’s website(s) or using the Let’s Go Makan! Services. You acknowledge that we do not operate or control the products or services offered by these Third Parties which are solely are responsible for all aspects of their relationship with you including accepting reservations, order processing, fulfilment, billing, cancellations and customer service. We are not a party to the transactions entered into between you and Third Parties and will not be held responsible for any aspect of any interactions, rights and obligations between you and the Third Parties.\n" +
        "\n" +
        "From time to time, we may provide the ability to pay for products or services on Let’s Go Makan!’s website(s) or using the Let’s Go Makan! Services. Such payment facilities are operated or owned by third parties. Please refer to the terms and conditions imposed by such payment service providers to understand your rights and responsibilities when using such payment facilities. You agree that you will not hold Let’s Go Makan! responsible for any aspect of any interactions, rights and obligations between you and any payment service providers.\n" +
        "\n" +
        "B. Privacy Policy\n" +
        "General Conditions\n" +
        "If you have any questions or concerns regarding this Policy, please contact out Data Protection Officer at personaldata@Let’s Go Makan!.com.\n" +
        "\n" +
        "Let’s Go Makan!’s website(s) or the Let’s Go Makan! Services may, from time to time, contain links or other means of access to other sites that are not owned or controlled by us. Please note that this Terms of Use and Privacy Policy does not apply to the practices of third parties that Let’s Go Makan! does not own or control, or individuals that we do not employ or manage. If you wish to know more about the manner in which third parties use or treat your personal data, kindly study their privacy policies or the terms of use of their services or contact them directly.\n" +
        "\n" +
        "By using Let’s Go Makan!’s website(s) and/or the Let’s Go Makan! Services, you agree to (i) the terms and conditions set out in this Policy including the types of your personal data that may be collected by us; (ii) the manner in which your personal data shall be collected, stored, transmitted, used or disclosed; and (iii) that the collection, use or disclosure of your personal data in the manner stated in this Policy are for purposes that a reasonable person would consider appropriate in the circumstances.\n" +
        "\n" +
        "Types of Personal Information We Collect and How We Use It\n" +
        "Our primary purpose in collecting personal information is to provide you with an efficient and customized experience. This allows us to provide services and features that are more likely to meet your personal interests and needs. We collect personal information about you that we consider necessary for achieving this purpose. We will not sell or disclose your personal information to third parties without your explicit consent.\n" +
        "\n" +
        "We collect the following information:\n" +
        "\n" +
        "Registration Information: We collect personal information when you register and create an account to use the Let’s Go Makan! Services. We use this information to contact you about Let’s Go Makan!’s website(s), the Let’s Go Makan! Services, your account and products and services in which you have expressed or display an interest. If you wish to subscribe to our newsletter(s) and/or promotional email, we will use your name and email to send the newsletter(s) and/or email to you. We will provide you a way to unsubscribe in all messages.\n" +
        "\n" +
        "Marketing and Other Promotional Material: We may also collect personal information about you if you provide such information in response to any marketing or promotional material, if you respond to our marketing or customer service officers or interact with any of our representatives, if you respond to any surveys, if you agree to be included in any marketing email or newsletters, or when you submit your personal data in any other situation.\n" +
        "\n" +
        "Browsing Information: We automatically receive and record information on our server logs from your browser, including your device’s IP address, cookies and the pages you view. This information may be used to further customize your experience on Let’s Go Makan! website(s) or the Let’s Go Makan! Services or to bring products or services which may be of interest to you to your attention.\n" +
        "\n" +
        "Cookies & Clear Gifs (Web Beacons/Web Bugs): We use technology such as cookies and clear gifs (also referred to as web beacons or web bugs) to help us manage content on Let’s Go Makan!’ website(s) and the Let’s Go Makan! Services by helping us keep track of the effectiveness of content. We use these tools for a number of purposes, including to help you access your information when you \"sign in\"; keep track of preferences you specify while you are using the Services; display product offerings; estimate and report our total audience size and traffic; and conduct research to improve our content and the Services. We store the information we collect using log files and clear gifs to create a profile of your preferences. We may associate your personally identifiable information, and your purchasing history, to information in your profile, in order to provide tailored promotions and market offers and/or to improve the content of the site for you. We do not share your profile with third parties.\n" +
        "\n" +
        "Posts & Correspondence: From time to time, we may provide you with the ability to post messages in a public forum, review a product or leave feedback for us. If you choose to use any of these functionalities, we will collect the information you post. We retain such information for internal analysis, resolve disputes, provide customer support and troubleshoot problems. If you send us personal correspondence, such as email, we may collect such information into a file specific to you. We may associate such information with other information in your profile, and store such information in order to provide tailored promotions and market offers and/or to improve the content of the site for you.\n" +
        "\n" +
        "“Wishlist(s)” and preferences: From time to time we may provide you with the opportunity to create “wishlist” and to indicate your preferences. If you choose to use any of these functionalities, we will collect the information you post. We may associate such information with other information in your profile, and store such information in order to provide tailored promotions and market offers and/or to improve the content of the site for you.\n" +
        "\n" +
        "Third Party Personal Information Provided by You: From time to time we may provide you with the opportunity to tell a friend about us and you may choose to provide your friend’s name and email for this purpose. By doing so, you represent and warrant that you have your friend’s consent to do so, and that your friend has consented to such personal data being used in accordance with this Policy.\n" +
        "\n" +
        "We share your personal information with the following:\n" +
        "\n" +
        "Third Party Services Providers: We may disclose your personally identifiable information to third party service providers that we use to provide Let’s Go Makan! website(s) and/or the Let’s Go Makan! Services to you. We retain the right to disclose non-personally identifiable information at our discretion. \n" +
        "\n" +
        "Disclosure for Legal Purposes: We reserve the right to disclose your personally identifiable information as required by law and when we believe that disclosure is necessary to protect our rights; to comply with a judicial proceeding, court order, or legal process served on us; or in connection with an actual or proposed corporate transaction or insolvency proceeding involving all or part of the business or asset to which the information pertains.\n" +
        "\n" +
        "Change of Control: We will transfer personally identifiable information about you if we are acquired by or merged with another company. In this event, we will notify you by email or by putting a prominent notice before information about you is transferred and becomes subject to a different privacy policy.\n" +
        "\n" +
        "We may provide a functionality that allows you to connect with social networking websites. We work with these sites’ application protocol interface (also known as developer API) to allow you to authorize us to access your account on such sites on your behalf. In order to provide this authorization, you will need to log-in to such sites directly through the Let’s Go Makan! Services. When authorized by you, such sites may recognize us when we ask, on your behalf, for access to your account information or to post information. For example, when you click “Like”, we will post the applicable information to your social network account. You can revoke our access to any such sites at any time by amending the appropriate settings from within your account settings on the applicable site.\n" +
        "\n" +
        "Right to Access and Correct Your Personal Data\n" +
        "If your personal data changes or is incorrect, or you no longer wish to use Let’s Go Makan!’s website(s) and/or the Let’s Go Makan! Services, you may correct, update, delete or deactivate your account by making the change to your account settings, or by emailing our Data Protection Officer at LetsGoMakan@application-5237c.firebaseapp.com. You may also contact us in the aforementioned ways to request for: (a) some or all of the personal data relating to you that are in our possession or control; and (b) information about the ways the personal data has been or may have been used or disclosed by us within a year before the date of your request.\n" +
        "\n" +
        "Period of Retention of Your Personal Data\n" +
        "We review the personal data we hold on a regular basis to determine if the data is still needed for the purposes it was collected.\n" +
        "\n" +
        "Security\n" +
        "The security of your personal information is important to us. We follow or require our service provider(s) to use generally accepted industry standards to protect the personal information submitted to us, both during transmission and once we receive it. However, no method of transmission over the internet, or method of electronic storage, can be 100% secure. Therefore, while we strive to use commercially acceptable means to protect your personal information, we cannot guarantee its security.\n" +
        "\n" +
        "Transfer of Your Personal Data\n" +
        "If we transfer personal data overseas, we will take appropriate steps to ensure that we will comply with the Personal Data Protection Act in respect of the transferred personal data while such personal data remains in our possession or control; and if the personal data is transferred to a recipient in a country or territory outside Singapore, that the recipient is bound by legally enforceable obligations to provide to the personal data transferred a standard of protection that is comparable to that under the Personal Data Protection Act.\n" +
        "\n" +
        "C. Miscellaneous Terms\n" +
        "If we fail to act with respect to your breach of these Terms of Use or the Privacy Policy or anyone else’s breach on any occasion, we are not waiving our right to act with respect to future or similar breaches.\n" +
        "\n" +
        "These Terms of use and Privacy Policy are governed by the laws of the Republic of Singapore. Any dispute arising out of or in connection with this contract, including any question regarding its existence, validity or termination, shall be referred to and finally resolved by arbitration in Singapore in accordance with the Arbitration Rules of the Singapore International Arbitration Centre (\"SIAC Rules\") for the time being in force, which rules are deemed to be incorporated by reference in this clause. The Tribunal shall consist of a single arbitrator. \n");
    }
}