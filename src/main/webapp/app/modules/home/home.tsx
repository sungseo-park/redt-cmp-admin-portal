import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';
import { getLoginUrl } from 'app/shared/util/url-utils';

export interface IHomeProp extends StateProps, DispatchProps {}

export class Home extends React.Component<IHomeProp> {
  componentDidMount() {
    this.props.getSession();
  }

  render() {
    const { account } = this.props;
    return (
      <Row>
        <Col md="9">
          <h2>
            <Translate contentKey="home.title">Welcome, Java Hipster!</Translate>
          </h2>
          {account && account.login ? (
            <div>
              <Alert color="success">
                <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                  You are logged in as user {account.login}.
                </Translate>
              </Alert>
            </div>
          ) : (
            <div>
              <Alert color="warning">
                <Translate contentKey="global.messages.info.authenticated.prefix">Click Here To </Translate>
                <a href={getLoginUrl()} className="alert-link">
                  <Translate contentKey="global.messages.info.authenticated.link">sign in</Translate>
                </a>
                {/*<Translate contentKey="global.messages.info.authenticated.suffix">*/}
                {/*  , you can try the default accounts:*/}
                {/*  <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)*/}
                {/*  <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).*/}
                {/*</Translate>*/}
              </Alert>
            </div>
          )}
          <p>
            <Translate contentKey="home.question">If you have any question on JHipster:</Translate>
          </p>

          <ul>
            <li>
              <a
                href="https://connect.weworkers.io/display/PS/Cost+Management+Platform+%28CMP%29+-+Technical+Home+Page"
                target="_blank"
                rel="noopener noreferrer"
              >
                <Translate contentKey="home.link.homepage">JHipster homepage</Translate>
              </a>
            </li>
            <li>
              <a href="https://connect.weworkers.io/display/PS/CMP+Reference+Docs" target="_blank" rel="noopener noreferrer">
                <Translate contentKey="home.link.stackoverflow">JHipster on Stack Overflow</Translate>
              </a>
            </li>
          </ul>

          {/*<p>*/}
          {/*  <Translate contentKey="home.like">If you like JHipster, do not forget to give us a star on</Translate>{' '}*/}
          {/*  <a href="https://github.com/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">*/}
          {/*    Github*/}
          {/*  </a>*/}
          {/*  !*/}
          {/*</p>*/}
        </Col>
        <Col md="3" className="pad">
          <span className="hipster rounded" />
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated
});

const mapDispatchToProps = { getSession };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Home);
