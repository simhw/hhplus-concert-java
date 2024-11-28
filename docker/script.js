import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
    scenarios: {
        contacts: {
            executor: 'ramping-vus',
            startVUs: 150,
            stages: [
                {duration: '10s', target: 160},
                {duration: '20s', target: 180},
                {duration: '30s', target: 200},
                {duration: '10s', target: 0},
            ],
            gracefulRampDown: '1s',
        },
    },

    thresholds: {
        'http_req_duration': ['p(99)<500'],
    },

    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)'],
};

// 1. setup: 대기열 토큰을 발급한다.
export function setup() {
    const res = http.post('http://host.docker.internal:8080/api/queue');
    if (res.status !== 200) {
        throw new Error(`failed to get token: ${res.status}`);
    }
    const data = res.json();
    if (!data.token) {
        throw new Error('token is missing in the response.');
    }
    return {token: data.token};
}

// 2. default: 데이터를 요청한다.
export default function (data) {
    reservation(data.token);
}

// 3. teardown (필요시 작성)
export function teardown(data) {
}

function concert(token) {
    const CONCERT_BASE_URL = 'http://host.docker.internal:8080/api/concerts';

    const params = {
        headers: {
            'Content-Type': 'application/json', 'Queue-Token': token,
        },
    };

    // 1. 콘서트를 조회한다.
    const res1 = http.get(CONCERT_BASE_URL, params);

    check(res1, {
        'get concert status is 200': (r) => r.status === 200,
    });

    if (res1.status !== 200) {
        console.error(`failed with status: ${res1.status}`);
    }
    sleep(1);

    // 2. 콘서트 공연을 조회한다.
    let concerts = res1.json();
    let concert_id = 1;

    const res2 = http.get(`${CONCERT_BASE_URL}/${concert_id}/performances`, params);
    check(res2, {
        'get performance status is 200': (r) => r.status === 200,
    });

    if (res2.status !== 200) {
        console.error(`failed with status: ${res2.status}`);
    }
    sleep(1);

    // 3. 공연의 좌석을 조회한다.
    let performances = res2.json();
    let performance_id = 119;

    const res3 = http.get(`${CONCERT_BASE_URL}/${concert_id}/performances/${performance_id}/seats`, params);
    check(res3, {
        'get seats status is 200': (r) => r.status === 200,
    });

    if (res3.status !== 200) {
        console.error(`failed with status: ${res3.status}`);
    }
}

function reservation(token) {
    const RESERVATION_BASE_URL = 'http://host.docker.internal:8080/api/reservations';

    const payload = {
        'userId': Math.floor(Math.random() * 1000) + 1,
        'concertId': 1,
        'performanceId': 119,
        'seatId': Math.floor(Math.random() * 1000) + 1,
    };

    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Queue-Token': token,
        },
    };

    const res = http.post(RESERVATION_BASE_URL, JSON.stringify(payload), params);
    check(res, {
        'reservation status is 200': (r) => r.status === 200,
    });
    if (res.status !== 200) {
        console.error(`Failed with status: ${res.status}`);
    }

}