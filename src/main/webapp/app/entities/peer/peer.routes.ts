import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PeerComponent } from './list/peer.component';
import { PeerDetailComponent } from './detail/peer-detail.component';
import { PeerUpdateComponent } from './update/peer-update.component';
import PeerResolve from './route/peer-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const peerRoute: Routes = [
  {
    path: '',
    component: PeerComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PeerDetailComponent,
    resolve: {
      peer: PeerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PeerUpdateComponent,
    resolve: {
      peer: PeerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PeerUpdateComponent,
    resolve: {
      peer: PeerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default peerRoute;
