// Copyright 2016 Proyectos y Sistemas de Mantenimiento SL (eProsima).
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @file HelloWorldPublisher.cpp
 *
 */

#include "HelloWorldPublisher.h"
#include <fastrtps/participant/Participant.h>
#include <fastrtps/attributes/ParticipantAttributes.h>
#include <fastrtps/attributes/PublisherAttributes.h>
#include <fastrtps/publisher/Publisher.h>
#include <fastrtps/Domain.h>
#include <fastrtps/utils/eClock.h>

#include <time.h>

#include <iostream>
#include <chrono>

#include <fstream>

using namespace std;
using namespace std::chrono;

using namespace eprosima::fastrtps;
using namespace eprosima::fastrtps::rtps;



HelloWorldPublisher::HelloWorldPublisher():mp_participant(nullptr),
mp_publisher(nullptr)
{


}

bool HelloWorldPublisher::init()
{
    m_Hello.index(0);
    m_Hello.message("HelloWorld");
    ParticipantAttributes PParam;
    PParam.rtps.defaultSendPort = 11511;
    PParam.rtps.use_IP6_to_send = true;
    PParam.rtps.builtin.use_SIMPLE_RTPSParticipantDiscoveryProtocol = true;
    PParam.rtps.builtin.use_SIMPLE_EndpointDiscoveryProtocol = true;
    PParam.rtps.builtin.m_simpleEDP.use_PublicationReaderANDSubscriptionWriter = true;
    PParam.rtps.builtin.m_simpleEDP.use_PublicationWriterANDSubscriptionReader = true;
    PParam.rtps.builtin.domainId = 0;
    PParam.rtps.builtin.leaseDuration = c_TimeInfinite;
    PParam.rtps.setName("Participant_pub");
    mp_participant = Domain::createParticipant(PParam);

    if(mp_participant==nullptr)
        return false;
    //REGISTER THE TYPE

    Domain::registerType(mp_participant,&m_type);

    //CREATE THE PUBLISHER

    PublisherAttributes Wparam;
    Wparam.topic.topicKind = NO_KEY;
    Wparam.topic.topicDataType = "HelloWorld";
    Wparam.topic.topicName = "HelloWorldTopic";
    Wparam.topic.historyQos.kind = KEEP_LAST_HISTORY_QOS;
    Wparam.topic.historyQos.depth = 30;
    Wparam.topic.resourceLimitsQos.max_samples = 50;
    Wparam.topic.resourceLimitsQos.allocated_samples = 20;
    Wparam.times.heartbeatPeriod.seconds = 2;
    Wparam.times.heartbeatPeriod.fraction = 200*1000*1000;
    Wparam.qos.m_reliability.kind = RELIABLE_RELIABILITY_QOS;
    mp_publisher = Domain::createPublisher(mp_participant,Wparam,(PublisherListener*)&m_listener);
    if(mp_publisher == nullptr)
        return false;

    return true;

}

HelloWorldPublisher::~HelloWorldPublisher()
{
    // TODO Auto-generated destructor stub
    Domain::removeParticipant(mp_participant);
}

void HelloWorldPublisher::PubListener::onPublicationMatched(Publisher* /*pub*/,MatchingInfo& info)
{
    if(info.status == MATCHED_MATCHING)
    {
        n_matched++;
        std::cout << "Publisher matched"<<std::endl;
    }
    else
    {
        n_matched--;
        std::cout << "Publisher unmatched"<<std::endl;
    }
}

void HelloWorldPublisher::run(uint32_t samples, uint32_t betweenMessage, char* logName)
{
/*
   for(uint32_t i = 0;i<samples;++i)
    {
        if(!publish())
            --i;
        else
        {
            std::cout << "Message: "<<m_Hello.message()<< " with index: "<< m_Hello.index()<< " SENT"<<std::endl;
        }
        eClock::my_sleep(25);
    }
*/
 
 ofstream myfile;
 time_t inicio = time(NULL);
 std::string s(logName);
 myfile.open("/opt/tmp/logFastRTPS_"+s+".log");
 time_t totalTranscurrido = 0;
 std::cout << "tiempo total: "<<samples<<std::endl;
 std::cout << "Antes de iniciar bucle"<<std::endl;

 high_resolution_clock::time_point bef, aft;
 
 do{

   bef = high_resolution_clock::now();
   if(publish()){
    aft = high_resolution_clock::now();
    auto duration = duration_cast<microseconds>(aft - bef).count();
    cout << "tiempo de petición: "<<duration<<endl;
    cout << "tamano enviado: "<<sizeof(m_Hello)<<endl;
    std::cout << "Message: "<<m_Hello.message()<< " with index: "<< m_Hello.index()<< " SENT"<<std::endl;
   // myfile << duration_cast<milliseconds>(system_clock::now().time_since_epoch()).count() << " Envio mensaje publisher, delay: " << duration << std::endl;
    myfile << duration_cast<milliseconds>(system_clock::now().time_since_epoch()).count() << "," << duration << "," << "Enviar mensaje" <<
         "," << "200,OK,Thread " << s << ",true,4740," << s << "," << s << "," << duration << ",1,0" << std::endl;
   }else{
    aft = high_resolution_clock::now();
    auto duration = duration_cast<microseconds>(aft - bef).count();
    myfile << duration_cast<milliseconds>(system_clock::now().time_since_epoch()).count() << "," << duration << "," << "Enviar mensaje" <<
	 "," << "500,KO,Thread " << s << ",false,4740," << s << "," << s << "," << duration << ",1,1" << std::endl;
   }
   eClock::my_sleep(betweenMessage);

   totalTranscurrido = time(NULL) - inicio;
   std::cout << "Total transcurrido: "<<totalTranscurrido<<std::endl;
 }while(totalTranscurrido < samples);
 myfile.close();

}

bool HelloWorldPublisher::publish()
{
    if(m_listener.n_matched>0)
    {
        m_Hello.index(m_Hello.index()+1);
        mp_publisher->write((void*)&m_Hello);
	//std::cout << "mensaje que envio: "<<m_Hello <<std::endl;
        return true;
    }
    return false;
}
