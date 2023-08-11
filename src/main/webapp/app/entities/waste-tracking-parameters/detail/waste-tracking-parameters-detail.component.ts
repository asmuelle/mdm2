import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IWasteTrackingParameters } from '../waste-tracking-parameters.model';

@Component({
  standalone: true,
  selector: 'jhi-waste-tracking-parameters-detail',
  templateUrl: './waste-tracking-parameters-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class WasteTrackingParametersDetailComponent {
  @Input() wasteTrackingParameters: IWasteTrackingParameters | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
